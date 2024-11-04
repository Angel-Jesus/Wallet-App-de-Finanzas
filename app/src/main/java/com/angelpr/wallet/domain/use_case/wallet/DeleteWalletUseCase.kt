package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.domain.repository.WalletRepository

class DeleteWalletUseCase(
    private val repository: WalletRepository
) {
    suspend fun card(card: CardModel) = repository.deleteCardRoom(card)
    suspend fun allDebtByCard(idCard: Int) = repository.deleteAllDebtRoom(idCard)
}