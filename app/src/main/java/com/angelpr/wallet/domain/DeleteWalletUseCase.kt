package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.ActionProcess
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun Card(id: Int): ActionProcess = repository.deleteCardToDatabase(id)
    suspend fun AllDebtByCard(idCard: Int): ActionProcess = repository.deleteAllDebtToDatabase(idCard)
}