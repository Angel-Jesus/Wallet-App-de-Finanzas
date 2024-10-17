package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import javax.inject.Inject

class SendWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun Card(card:CardModel):ActionProcess = repository.addCardToDatabase(card)
}