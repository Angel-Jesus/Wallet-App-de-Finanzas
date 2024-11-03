package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository
import com.angelpr.wallet.presentation.components.model.Type


class GetWalletUseCase(
    private val repository: WalletRepository
) {
    suspend fun allCard(): List<CardModel> = repository.getAllCardRoom()

    suspend fun getLineUseCard(idCard: Int, dateInit: Long, dateEnd: Long): Float =
        repository.getLineUseRoom(idCard = idCard, dateInit = dateInit, dateEnd = dateEnd)

    suspend fun getDebtNotPaidCard(idCard: Int): List<DebtModel> =
        repository.getDebtNotPaidTRoom(idCard)

    suspend fun getDebtPaidCard(idCard: Int, limit: Int): List<DebtModel> =
        repository.getDebtPaidRoom(idCard = idCard, limit = limit)

    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type> =
        repository.getTotalDebtType(debtList)
}