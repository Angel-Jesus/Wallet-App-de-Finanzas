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
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.utils.sumOfFloat
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

    suspend fun getLineUseToDatabase(idCard: Int, dateInit: Long, dateEnd: Long): Float =
        walletDao.getLineUseCard(idCard = idCard, dateInit = dateInit, dateEnd = dateEnd)


    suspend fun getDebtNotPaidToDatabase(idCard: Int): List<DebtModel> {
        val response: List<DebtsWalletEntity> = walletDao.getDebtNotPaidCardById(idCard = idCard)
        if (response.isNotEmpty()) {
            return response.map { it.toDebtsWallet() }
        }
        return emptyList()
    }

    suspend fun getDebtPaidToDatabase(idCard: Int, limit: Int): List<DebtModel> {
        val response: List<DebtsWalletEntity> = walletDao.getDebtPaidCardById(idCard = idCard, limit = limit)
        if (response.isNotEmpty()) {
            return response.map { it.toDebtsWallet() }
        }
        return emptyList()
    }

    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type> {
        val debt = mutableMapOf<String, Type>()
        val debtType = debtList.groupBy { it.type }
        debtType.forEach{ (type, list) ->

            val debtSum = list.sumOfFloat{if(it.quotas > 1) { it.debt/it.quotas } else { it.debt } }

            when(type){
                Categories.Debt[0].name -> debt[Categories.Debt[0].name] = Categories.Debt[0].copy(value = debtSum)
                Categories.Debt[1].name -> debt[Categories.Debt[1].name] = Categories.Debt[1].copy(value = debtSum)
                Categories.Debt[2].name -> debt[Categories.Debt[2].name] = Categories.Debt[2].copy(value = debtSum)
                Categories.Debt[3].name -> debt[Categories.Debt[3].name] = Categories.Debt[3].copy(value = debtSum)
                Categories.Debt[4].name -> debt[Categories.Debt[4].name] = Categories.Debt[4].copy(value = debtSum)
            }
        }

        return debt
    }

    suspend fun addDebtToDatabase(debt: DebtModel): ActionProcess {
        val debtEntity: DebtsWalletEntity = debt.toDebtsWalletEntity()
        walletDao.insertDebtCard(debtEntity)
        return ActionProcess.SUCCESS
    }

    suspend fun updateDebtToDatabase(id: Int, quotasPaid: Int, isPaid: Int): ActionProcess {
        walletDao.updateDebtCard(id = id, quotasPaid = quotasPaid, isPaid = isPaid)
        return ActionProcess.UPDATE_DEBT_BY_CARD
    }

    suspend fun deleteDebtToDatabase(id: Int): ActionProcess {
        walletDao.deleteDebtCard(id)
        return ActionProcess.SUCCESS
    }

    suspend fun deleteAllDebtToDatabase(idCard: Int): ActionProcess {
        walletDao.deleteAllDebtCard(idCard)
        return ActionProcess.SUCCESS
    }




}