package com.angelpr.wallet.domain.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.data.repository.WalletRepository

class DeleteWalletUseCase(
    private val repository: WalletRepository
) {
    suspend fun card(card: CardModel) = repository.deleteCardRoom(card)
    suspend fun allDebtByCard(idCard: Int) = repository.deleteAllDebtRoom(idCard)
    suspend fun debtByCard(debtModel: DebtModel) = repository.deleteDebtRoom(debtModel)
}