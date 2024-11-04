package com.angelpr.wallet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.use_case.DataStoreUseCase
import com.angelpr.wallet.domain.use_case.NotificationUseCase
import com.angelpr.wallet.domain.use_case.wallet.WalletUseCases
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.screen.event.CardsEvent
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

    private val _totalDebtCard = MutableStateFlow(0.0f)
    val totalDebtCard = _totalDebtCard.asStateFlow()

    private val _totalDebtType = MutableStateFlow(emptyMap<String, Type>())
    val totalDebtType = _totalDebtType.asStateFlow()

    private val _eventFlowAddEditCard = MutableSharedFlow<UiEvent>()
    val eventFlowAddEditCard = _eventFlowAddEditCard.asSharedFlow()

    private val _cardSelected = MutableStateFlow<CardModel?>(null)
    val cardSelected = _cardSelected.asStateFlow()

    init {
        getCards()
    }

    private var getCardsJob: Job? = null

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

    // Action by Cards
    fun onEventCard(event: CardsEvent) {
        when (event) {
            is CardsEvent.GetCards -> getCards()

            is CardsEvent.AddCard -> {
                viewModelScope.launch {
                    walletUseCases.addWallet.card(event.card)
                    _eventFlowAddEditCard.emit(UiEvent.SaveCard)
                }
            }

            is CardsEvent.DeleteCard -> {
                viewModelScope.launch {
                    walletUseCases.deleteWallet.card(event.card)
                    _stateCard.update { it.copy(cardSelected = null) }
                    _eventFlowAddEditCard.emit(UiEvent.DeleteCard)
                }
            }

            is CardsEvent.UpdateCard -> {
                viewModelScope.launch {
                    walletUseCases.updateWallet.card(event.card)
                }
            }

            is CardsEvent.CardSelected -> {
                _stateCard.update { it.copy(cardSelected = event.card) }
                //_cardSelected.update { event.card }
            }
        }
    }

    fun getLineUseCard(id: Int, dateClose: Int) {
        viewModelScope.launch {
            val init = getInitDate(dateClose)
            val end = init.plusMonths(1)

            val initDate = init.toEpochDay()
            val endDate = end.toEpochDay()

            _totalDebtCard.update { walletUseCases.getWallet.getLineUseCard(id, initDate, endDate) }
        }
    }

    // Action by Debts
    fun addDebt(debt: DebtModel) {
        viewModelScope.launch {
            walletUseCases.addWallet.debt(debt)

            // Revisar
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update { it.copy(state = ActionProcess.SUCCESS) }
        }
    }

    fun getDebtByCard(idCard: Int, limit: Int = 20) {
        viewModelScope.launch {

            _stateDebt.update {
                it.copy(
                    state = ActionProcess.DEBT_BY_CARD,
                    debtNotPaidList = walletUseCases.getWallet.getDebtNotPaidCard(idCard),
                    debtPaidList = walletUseCases.getWallet.getDebtPaidCard(idCard, limit)
                )
            }

            // Revisar
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
        }
    }

    fun getDebtByType(debtList: List<DebtModel>) {
        _totalDebtType.update { walletUseCases.getWallet.getTotalDebtType(debtList) }
    }

    fun updateDebtState(debt: DebtModel, id: Int, quotas: Int, quotasPaid: Int, date: Long) {
        viewModelScope.launch {

            walletUseCases.updateWallet.debtState(debt)
            // Revisar
            /*
            _stateDebt.update {
                it.copy(
                    state = walletUseCases.updateWallet.DebtState(
                        id,
                        quotas,
                        quotasPaid,
                        date
                    )
                )
            }

             */

            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
        }
    }

    fun deleteAllDebt(idCard: Int) {
        viewModelScope.launch {
            walletUseCases.deleteWallet.allDebtByCard(idCard)

            // Revisar
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update { it.copy(state = ActionProcess.SUCCESS) }
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

    fun cancelScheduleNotification(cardName: String, dateExpired: LocalDate) {
        val notificationId =
            dateExpired.year * 10000 + dateExpired.monthValue * 100 + dateExpired.dayOfMonth + cardName.hashCode()
        notificationUseCase.cancel(notificationId)
    }

    // Extra Function
    fun getDateExpired(dayExpired: Int, dateClose: Int, dateToday: LocalDate): Long {
        val dateMonthToday = LocalDate.of(dateToday.year, dateToday.month.value, dayExpired)

        val date = if (dateToday.dayOfMonth > dateClose) {
            dateMonthToday.plusMonths(2)
        } else {
            dateMonthToday.plusMonths(1)
        }

        return date.toEpochDay()
    }

    private fun getInitDate(date: Int): LocalDate {
        val today = LocalDate.now()
        return LocalDate.of(today.year, today.month, date)
    }

    private fun getCards() {
        getCardsJob?.cancel()
        getCardsJob = walletUseCases.getWallet.allCard()
            .onEach { cards ->
                Log.d("viewModel", "start getCards")
                _stateCard.value = stateCard.value.copy(
                    cardList = cards,
                    cardSelected = if(stateCard.value.cardSelected == null) cards.firstOrNull() else stateCard.value.cardSelected,
                )
            }
            .launchIn(viewModelScope)
    }

    // DataClass of State
    data class UiStateCard(
        val cardList: List<CardModel> = emptyList(),
        val cardSelected: CardModel? = null
    )

    data class UiStateDebt(
        val debtNotPaidList: List<DebtModel> = emptyList(),
        val debtPaidList: List<DebtModel> = emptyList(),
        var state: ActionProcess = ActionProcess.NOT_AVAILABLE
    )

    sealed class UiEvent{
        data object SaveCard: UiEvent()
        data object DeleteCard: UiEvent()
    }

}