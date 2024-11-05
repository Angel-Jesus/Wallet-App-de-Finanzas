package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository
import com.angelpr.wallet.utils.getMonthInCase

class UpdateWalletUseCase (
    private val repository: WalletRepository
) {
    suspend fun card(cardModel: CardModel) {
        repository.updateCardRoom(cardModel)
    }

    suspend fun debtState(debt: DebtModel){
        val isPaid = if(debt.quotePaid == debt.quotas) 1 else 0
        val dateExpired = getMonthInCase(debt.dateExpired, debt.quotePaid == debt.quotas)
        repository.updateDebtRoom(debt.copy(isPaid = isPaid, dateExpired = dateExpired))
    }

}