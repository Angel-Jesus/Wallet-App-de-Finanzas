package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.domain.repository.WalletRepository

class DeleteWalletUseCase(
    private val repository: WalletRepository
) {
    suspend fun card(id: Int) = repository.deleteCardRoom(id)
    suspend fun allDebtByCard(idCard: Int) = repository.deleteAllDebtRoom(idCard)
}