package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository

class UpdateWalletUseCase (
    private val repository: WalletRepository
) {
    suspend fun card(cardModel: CardModel) {
        repository.updateCardRoom(cardModel)
    }

    suspend fun debtState(debt: DebtModel){
        repository.updateDebtRoom(debt)
    }

}