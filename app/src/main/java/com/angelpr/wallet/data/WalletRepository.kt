package com.angelpr.wallet.data

import com.angelpr.wallet.data.db.dao.WalletDao
import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity
import com.angelpr.wallet.data.db.entities.toCardWallet
import com.angelpr.wallet.data.db.entities.toCardWalletEntity
import com.angelpr.wallet.data.db.entities.toDebtsWallet
import com.angelpr.wallet.data.db.entities.toDebtsWalletEntity
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import javax.inject.Inject

class WalletRepository @Inject constructor(
    private val walletDao: WalletDao
) {

    // Action by Cards
    suspend fun getAllCardFromDatabase(): List<CardModel> {
        val response: List<CardWalletEntity> = walletDao.getAllCards()
        if (response.isNotEmpty()) {
            return response.map { it.toCardWallet() }
        }
        return emptyList()
    }

    suspend fun addCardToDatabase(card: CardModel): ActionProcess {
        val cardEntity = card.toCardWalletEntity()
        walletDao.insertCard(cardEntity)
        return ActionProcess.SUCCESS
    }

    suspend fun updateCardToDatabase(card: CardModel): ActionProcess {
        val cardEntity = card.toCardWalletEntity()
        walletDao.updateCard(cardEntity)
        return ActionProcess.SUCCESS
    }

    suspend fun deleteCardToDatabase(id: Int): ActionProcess {
        walletDao.deleteCard(id)
        return ActionProcess.SUCCESS
    }

    // Action by Debts

    suspend fun getDateDebtToDatabase(id: Int, dateInit: Long, dateEnd: Long): List<DebtModel> {
        val response: List<DebtsWalletEntity> =
            walletDao.getDebtCardByDate(id = id, dateInit = dateInit, dateEnd = dateEnd)
        if (response.isNotEmpty()) {
            return response.map { it.toDebtsWallet() }
        }
        return emptyList()
    }


    suspend fun getDebtToDatabase(id: Int): List<DebtModel> {
        val response: List<DebtsWalletEntity> = walletDao.getDebtCardById(id)
        if (response.isNotEmpty()) {
            return response.map { it.toDebtsWallet() }
        }
        return emptyList()
    }

    suspend fun addDebtToDatabase(debt: DebtModel): ActionProcess {
        val debtEntity: DebtsWalletEntity = debt.toDebtsWalletEntity()
        walletDao.insertDebtCard(debtEntity)
        return ActionProcess.SUCCESS
    }

    suspend fun updateDebtToDatabase(id: Int, isPaid: Int): ActionProcess {
        walletDao.updateDebtCard(id = id, isPaid = isPaid)
        return ActionProcess.SUCCESS
    }

    suspend fun deleteDebtToDatabase(id: Int): ActionProcess {
        walletDao.deleteDebtCard(id)
        return ActionProcess.SUCCESS
    }


}