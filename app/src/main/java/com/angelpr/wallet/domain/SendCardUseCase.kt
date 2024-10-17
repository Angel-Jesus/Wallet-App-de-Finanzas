package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import javax.inject.Inject

class SendWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun Card(card:CardModel):ActionProcess = repository.addCardToDatabase(card)

    suspend fun Debt(debt: DebtModel): ActionProcess = repository.addDebtToDatabase(debt)
}