package com.angelpr.wallet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.use_case.DataStoreUseCase
import com.angelpr.wallet.domain.use_case.NotificationUseCase
import com.angelpr.wallet.domain.use_case.wallet.WalletUseCases
import com.angelpr.wallet.presentation.components.model.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Permission
    fun updateShowDialog(show: Boolean) {
        _showDialog.update { show }
    }

    fun updateLaunchSetting(launch: Boolean) {
        _launchSetting.update { launch }
    }

    // DataStore
    fun updateEnableNotification(enable: Boolean){
        viewModelScope.launch {
            dataStoreUseCase.updateNotification(enable)
        }
    }

    fun getEnableNotification(){
        viewModelScope.launch {
            dataStoreUseCase.getNotification().collect{enable ->
                _enableNotification.update { enable }
            }
        }
    }

    // Action by Cards
    fun getAllCard() {
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            val result = walletUseCases.getWallet.allCard()
            _stateCard.update { it.copy(state = ActionProcess.ALL_CARD, cardList = result) }
        }
    }

    fun addNewCard(cardModel: CardModel) {
        viewModelScope.launch {
            walletUseCases.addWallet.card(cardModel)

            // Revisar esto
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = ActionProcess.SUCCESS) }
        }
    }

    fun updateCard(cardModel: CardModel) {
        viewModelScope.launch {
            walletUseCases.updateWallet.card(cardModel)

            // Revisar
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = ActionProcess.SUCCESS) }
        }
    }

    fun deleteCard(id: Int) {
        viewModelScope.launch {
            walletUseCases.deleteWallet.card(id)

            // Revisar
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = ActionProcess.SUCCESS) }
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
            day = day)
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

    // DataClass of State

    data class UiStateCard(
        val cardList: List<CardModel> = emptyList(),
        var state: ActionProcess = ActionProcess.NOT_AVAILABLE
    )

    data class UiStateDebt(
        val debtNotPaidList: List<DebtModel> = emptyList(),
        val debtPaidList: List<DebtModel> = emptyList(),
        var state: ActionProcess = ActionProcess.NOT_AVAILABLE
    )

    private fun getInitDate(date: Int): LocalDate {
        val today = LocalDate.now()
        return LocalDate.of(today.year, today.month, date)
    }

}