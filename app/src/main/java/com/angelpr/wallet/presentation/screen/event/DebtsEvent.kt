package com.angelpr.wallet.presentation.screen.event

import com.angelpr.wallet.data.model.DebtModel

sealed class DebtsEvent {
    data object GetDebts: DebtsEvent()
    data class AddDebts(val debt: DebtModel): DebtsEvent()
    data class DeleteDebt(val debt: DebtModel): DebtsEvent()
    data class DeleteAllDebts(val idCard: Int): DebtsEvent()
    data class UpdateDebts(val debt: DebtModel): DebtsEvent()
}