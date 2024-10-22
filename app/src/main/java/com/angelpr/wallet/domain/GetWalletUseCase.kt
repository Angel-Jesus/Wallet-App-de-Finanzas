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

    suspend fun GetLineUseCard(idCard: Int, dateInit: Long, dateEnd: Long): Float =
        repository.getLineUseToDatabase(idCard = idCard, dateInit = dateInit, dateEnd = dateEnd)

    suspend fun GetDebtNotPaidCard(idCard: Int): List<DebtModel> =
        repository.getDebtNotPaidToDatabase(idCard)

    suspend fun GetDebtPaidCard(idCard: Int, limit: Int): List<DebtModel> =
        repository.getDebtPaidToDatabase(idCard = idCard, limit = limit)

    fun GetTotalDebtType(debtList: List<DebtModel>): Map<String, Type> =
        repository.getTotalDebtType(debtList)
}