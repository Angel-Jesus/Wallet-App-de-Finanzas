package com.angelpr.wallet.domain.repository

import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.model.Type
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    // Action by Cards
    fun getCardsRoom(): Flow<List<CardWalletEntity>>

    suspend fun addCardRoom(card: CardModel)

    suspend fun updateCardRoom(card: CardModel)

    suspend fun deleteCardRoom(card: CardModel)

    // Action by Debts
    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type>

    suspend fun getLineUseRoom(idCard: Int, dateInit: Long, dateEnd: Long): Float

    fun getDebtByCardRoom(idCard: Int): Flow<List<DebtsWalletEntity>>

    suspend fun addDebtRoom(debt: DebtModel)

    suspend fun updateDebtRoom(debt: DebtModel)

    suspend fun deleteDebtRoom(debt: DebtModel)

    suspend fun deleteAllDebtRoom(idCard: Int)
}