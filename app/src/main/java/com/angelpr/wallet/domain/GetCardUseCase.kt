package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.CardModel
import javax.inject.Inject


class GetWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun AllCard(): List<CardModel> = repository.getAllCardFromDatabase()

    suspend fun GetLineUseCard(id: Int, dateInit: Long, dateEnd: Long): Float =
        repository.getLineUseToDatabase(id = id, dateInit = dateInit, dateEnd = dateEnd)
}