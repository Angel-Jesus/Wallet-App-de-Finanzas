package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.model.Type
import javax.inject.Inject


class GetWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun AllCard(): List<CardModel> = repository.getAllCardFromDatabase()

    suspend fun GetLineUseCard(id: Int, dateInit: Long, dateEnd: Long): Float =
        repository.getLineUseToDatabase(id = id, dateInit = dateInit, dateEnd = dateEnd)

    suspend fun GetDebtCard(id: Int): List<DebtModel> = repository.getDebtToDatabase(id)

    fun GetTotalDebtType(debtList: List<DebtModel>): Map<String, Type> = repository.getTotalDebtType(debtList)
}