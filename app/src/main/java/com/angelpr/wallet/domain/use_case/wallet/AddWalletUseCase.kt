package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository

class AddWalletUseCase(
    private val repository: WalletRepository
) {
    suspend fun card(card:CardModel) = repository.addCardRoom(card)

    suspend fun debt(debt: DebtModel) = repository.addDebtRoom(debt)
}