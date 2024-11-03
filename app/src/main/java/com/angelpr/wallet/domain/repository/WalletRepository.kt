package com.angelpr.wallet.domain.repository

import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.model.Type

interface WalletRepository {
    // Action by Cards
    suspend fun getAllCardRoom(): List<CardModel>

    suspend fun addCardRoom(card: CardModel)

    suspend fun updateCardRoom(card: CardModel)

    suspend fun deleteCardRoom(id: Int)

    // Action by Debts
    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type>

    suspend fun getLineUseRoom(idCard: Int, dateInit: Long, dateEnd: Long): Float

    suspend fun getDebtNotPaidTRoom(idCard: Int): List<DebtModel>

    suspend fun getDebtPaidRoom(idCard: Int, limit: Int): List<DebtModel>

    suspend fun addDebtRoom(debt: DebtModel)

    suspend fun updateDebtRoom(debt: DebtModel)

    suspend fun deleteDebtRoom(id: Int)

    suspend fun deleteAllDebtRoom(idCard: Int)
}