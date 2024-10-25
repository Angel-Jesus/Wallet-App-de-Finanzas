package com.angelpr.wallet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.DeleteWalletUseCase
import com.angelpr.wallet.domain.GetWalletUseCase
import com.angelpr.wallet.domain.ScheduleNotificationUseCase
import com.angelpr.wallet.domain.SendWalletUseCase
import com.angelpr.wallet.domain.UpdateWalletUseCase
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
    private val getWalletUseCase: GetWalletUseCase,
    private val sendWalletUseCase: SendWalletUseCase,
    private val updateWalletUseCase: UpdateWalletUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val notificationUseCase: ScheduleNotificationUseCase
) : ViewModel() {

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

    // Action by Cards
    fun getAllCard() {
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            val result = getWalletUseCase.AllCard()
            _stateCard.update { it.copy(state = ActionProcess.ALL_CARD, cardList = result) }
        }
    }

    fun addNewCard(cardModel: CardModel) {
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = sendWalletUseCase.Card(cardModel)) }
        }
    }

    fun updateCard(cardModel: CardModel) {
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = updateWalletUseCase.Card(cardModel)) }
        }
    }

    fun deleteCard(id: Int) {
        viewModelScope.launch {
            _stateCard.update { it.copy(state = ActionProcess.LOADING) }
            _stateCard.update { it.copy(state = deleteWalletUseCase.Card(id)) }
        }
    }

    fun getLineUseCard(id: Int, dateClose: Int) {
        viewModelScope.launch {
            val init = getInitDate(dateClose)
            val end = init.plusMonths(1)

            val initDate = init.toEpochDay()
            val endDate = end.toEpochDay()

            _totalDebtCard.update { getWalletUseCase.GetLineUseCard(id, initDate, endDate) }
        }
    }

    // Action by Debts
    fun addDebt(debt: DebtModel) {
        viewModelScope.launch {
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update { it.copy(state = sendWalletUseCase.Debt(debt)) }
        }
    }

    fun getDebtByCard(idCard: Int, limit: Int = 20) {
        viewModelScope.launch {
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update {
                it.copy(
                    state = ActionProcess.DEBT_BY_CARD,
                    debtNotPaidList = getWalletUseCase.GetDebtNotPaidCard(idCard),
                    debtPaidList = getWalletUseCase.GetDebtPaidCard(idCard, limit)
                )
            }
        }
    }

    fun getDebtByType(debtList: List<DebtModel>) {
        _totalDebtType.update { getWalletUseCase.GetTotalDebtType(debtList) }
    }

    fun updateDebtState(id: Int, quotas: Int, quotasPaid: Int) {
        viewModelScope.launch {
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update {
                it.copy(
                    state = updateWalletUseCase.DebtState(
                        id,
                        quotas,
                        quotasPaid
                    )
                )
            }
        }
    }

    fun deleteAllDebt(idCard: Int) {
        viewModelScope.launch {
            _stateDebt.update { it.copy(state = ActionProcess.LOADING) }
            _stateDebt.update { it.copy(state = deleteWalletUseCase.AllDebtByCard(idCard)) }
        }
    }

    // Notification and AlarmManager
    fun setScheduleNotification(daysToSubtract: Long, date: LocalDate) {
        val notificationId = date.year * 10000 + date.monthValue * 100 + date.dayOfMonth

        val dateRecordatory = date.minusDays(daysToSubtract)
        val year = dateRecordatory.year
        val month = dateRecordatory.month.value
        val day = dateRecordatory.dayOfMonth

        notificationUseCase.schedule(notificationId, year, month, day)
    }

    fun cancelScheduleNotification(notificationId: Int) {
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