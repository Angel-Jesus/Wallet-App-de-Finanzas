package com.angelpr.wallet.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.use_case.DataStoreUseCase
import com.angelpr.wallet.domain.use_case.NotificationUseCase
import com.angelpr.wallet.domain.use_case.wallet.WalletUseCases
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.screen.event.CardsEvent
import com.angelpr.wallet.presentation.screen.event.DebtsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val notificationUseCase: NotificationUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : ViewModel() {

    private val stateInit = mutableStateOf(true)

    private val _enableNotification = MutableStateFlow(false)
    val enableNotification = _enableNotification.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _launchSetting = MutableStateFlow(false)
    val launchSetting = _launchSetting.asStateFlow()

    private val _stateCard = MutableStateFlow(UiStateCard())
    val stateCard = _stateCard.asStateFlow()

    private val _stateDebt = MutableStateFlow(UiStateDebt())
    val stateDebt = _stateDebt.asStateFlow()

    private val _eventFlowAddEditCard = MutableSharedFlow<UiEvent>()
    val eventFlowAddEditCard = _eventFlowAddEditCard.asSharedFlow()

    private val _eventFlowAddEditDebt = MutableSharedFlow<UiEvent>()
    val eventFlowAddEditDebt = _eventFlowAddEditDebt.asSharedFlow()

    init {
        getCards()
    }

    private var getCardsJob: Job? = null
    private var getDebtsJob: Job? = null

    // Permission
    fun updateShowDialog(show: Boolean) {
        _showDialog.update { show }
    }

    fun updateLaunchSetting(launch: Boolean) {
        _launchSetting.update { launch }
    }

    // DataStore
    fun updateEnableNotification(enable: Boolean) {
        viewModelScope.launch {
            dataStoreUseCase.updateNotification(enable)
        }
    }

    fun getEnableNotification() {
        viewModelScope.launch {
            dataStoreUseCase.getNotification().collect { enable ->
                _enableNotification.update { enable }
            }
        }
    }

    // Events
    fun onEventCard(event: CardsEvent) {
        when (event) {
            is CardsEvent.GetCards -> getCards()

            is CardsEvent.AddCard -> {
                viewModelScope.launch {
                    walletUseCases.addWallet.card(event.card)
                    _eventFlowAddEditCard.emit(UiEvent.Success)
                }
            }

            is CardsEvent.DeleteCard -> {
                viewModelScope.launch {
                    walletUseCases.deleteWallet.card(event.card)
                    _stateCard.update { it.copy(cardSelected = null) }
                    _eventFlowAddEditCard.emit(UiEvent.Success)
                }
            }

            is CardsEvent.UpdateCard -> {
                viewModelScope.launch {
                    walletUseCases.updateWallet.card(event.card)
                    _stateCard.update { it.copy(cardSelected = event.card) }
                    _eventFlowAddEditCard.emit(UiEvent.Success)
                }
            }

            is CardsEvent.CardSelected -> {
                viewModelScope.launch {
                    _stateCard.update {
                        it.copy(
                            cardSelected = event.card,
                            lineUseCard = walletUseCases.getWallet.getLineUseCard(event.card)
                        )
                    }

                    getDebtsByCard(event.card.id)
                }

            }
        }
    }

    fun onEventDebt(event: DebtsEvent) {
        when (event) {
            is DebtsEvent.GetDebts -> getDebtsByCard(stateCard.value.cardSelected?.id ?: 0)

            is DebtsEvent.AddDebts -> {
                viewModelScope.launch {
                    walletUseCases.addWallet.debt(event.debt)
                    _eventFlowAddEditDebt.emit(UiEvent.Success)
                }
            }

            is DebtsEvent.UpdateDebts -> {
                viewModelScope.launch {
                    walletUseCases.updateWallet.debtState(event.debt)
                    //_eventFlowAddEditDebt.emit(UiEvent.Success)
                }
            }

            is DebtsEvent.DeleteDebt -> {
                viewModelScope.launch {
                    walletUseCases.deleteWallet.debtByCard(event.debt)
                    //_eventFlowAddEditDebt.emit(UiEvent.Success)
                }
            }

            is DebtsEvent.DeleteAllDebts -> {
                viewModelScope.launch {
                    walletUseCases.deleteWallet.allDebtByCard(event.idCard)
                    //_eventFlowAddEditDebt.emit(UiEvent.Success)
                }
            }
        }
    }

    // Notification and AlarmManager
    fun setScheduleNotification(cardName: String, daysToSubtract: Long, dateExpired: LocalDate) {
        val notificationId =
            dateExpired.year * 10000 + dateExpired.monthValue * 100 + dateExpired.dayOfMonth + cardName.hashCode()

        val dateRecordatory = dateExpired.minusDays(daysToSubtract)
        val year = dateRecordatory.year
        val month = dateRecordatory.month.value
        val day = dateRecordatory.dayOfMonth

        notificationUseCase.schedule(
            cardName = cardName,
            dateExpired = dateExpired,
            notificationId = notificationId,
            year = year,
            month = month,
            day = day
        )
    }

    fun cancelScheduleNotification(cardName: String, dates: List<Long>) {
        for (date in dates) {
            val dateExpired = LocalDate.ofEpochDay(date)
            val notificationId =
                dateExpired.year * 10000 + dateExpired.monthValue * 100 + dateExpired.dayOfMonth + cardName.hashCode()
            notificationUseCase.cancel(notificationId)
        }
    }

    // Jobs
    private fun getCards() {
        getCardsJob?.cancel()
        getCardsJob = walletUseCases.getWallet.allCard()
            .onEach { cards ->
                val isNullCardSelected = stateCard.value.cardSelected == null
                val cardInit = cards.firstOrNull()
                _stateCard.value = stateCard.value.copy(
                    cardList = cards,
                    cardSelected = if (isNullCardSelected) cardInit else stateCard.value.cardSelected
                )

                // Only execute first time
                if (stateInit.value) {
                    stateInit.value = false
                    getDebtsByCard(_stateCard.value.cardSelected?.id ?: 0)
                }

            }
            .launchIn(viewModelScope)
    }

    private fun getDebtsByCard(id: Int, limit: Int = 50) {
        getDebtsJob?.cancel()
        getDebtsJob = walletUseCases.getWallet.getDebtCard(idCard = id)
            .onEach { debts ->
                val debtsNotPaid = debts.filter { it.isPaid == 0 }
                val debtsPaid = debts.filter { it.isPaid == 1 }

                _stateDebt.value = stateDebt.value.copy(
                    debtNotPaidList = debtsNotPaid,
                    debtPaidList = debtsPaid.take(limit),
                    totalDebtByType = walletUseCases.getWallet.getTotalDebtType(debtsNotPaid)
                )

                _stateCard.value = stateCard.value.copy(
                    lineUseCard = walletUseCases.getWallet.getLineUseCard(stateCard.value.cardSelected!!)
                )
            }
            .launchIn(viewModelScope)
    }

    // DataClass of State
    data class UiStateCard(
        val cardList: List<CardModel> = emptyList(),
        val cardSelected: CardModel? = null,
        val lineUseCard: Float = 0.0f
    )

    data class UiStateDebt(
        val debtNotPaidList: List<DebtModel> = emptyList(),
        val debtPaidList: List<DebtModel> = emptyList(),
        val totalDebtByType: Map<String, Type> = emptyMap()
    )

    // UiEvents
    sealed class UiEvent {
        data object Success : UiEvent()
    }

}