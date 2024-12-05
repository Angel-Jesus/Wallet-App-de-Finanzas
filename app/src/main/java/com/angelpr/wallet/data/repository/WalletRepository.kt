package com.angelpr.wallet.data.repository

import com.angelpr.wallet.data.db.dao.WalletDao
import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity
import com.angelpr.wallet.data.db.entities.toCardWalletEntity
import com.angelpr.wallet.data.db.entities.toDebtsWalletEntity
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.utils.sumOfFloat
import kotlinx.coroutines.flow.Flow

class WalletRepository(
    private val walletDao: WalletDao
) {

    // Action by Cards
    fun getCardsRoom(): Flow<List<CardWalletEntity>> {
        return walletDao.getCards()
    }

    suspend fun addCardRoom(card: CardModel) {
        walletDao.insertCard(card.toCardWalletEntity())
    }

    suspend fun updateCardRoom(card: CardModel) {
        walletDao.updateCard(card.toCardWalletEntity())
    }

    suspend fun deleteCardRoom(card: CardModel) {
        walletDao.deleteCard(card.toCardWalletEntity())
    }

    // Action by Debts
    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type> {
        val debt = mutableMapOf<String, Type>()
        val debtType = debtList.groupBy { it.type }
        debtType.forEach { (type, list) ->

            val debtSum = list.sumOfFloat {
                if (it.quotas > 1) {
                    it.debt / it.quotas
                } else {
                    it.debt
                }
            }

            when (type) {
                Categories.Debt[0].name -> debt[Categories.Debt[0].name] =
                    Categories.Debt[0].copy(value = debtSum)

                Categories.Debt[1].name -> debt[Categories.Debt[1].name] =
                    Categories.Debt[1].copy(value = debtSum)

                Categories.Debt[2].name -> debt[Categories.Debt[2].name] =
                    Categories.Debt[2].copy(value = debtSum)

                Categories.Debt[3].name -> debt[Categories.Debt[3].name] =
                    Categories.Debt[3].copy(value = debtSum)

                Categories.Debt[4].name -> debt[Categories.Debt[4].name] =
                    Categories.Debt[4].copy(value = debtSum)
            }
        }
        return debt
    }

    suspend fun getLineUseRoom(idCard: Int, dateInit: Long, dateEnd: Long): Float {
        return walletDao.getLineUseCard(idCard = idCard, dateInit = dateInit, dateEnd = dateEnd)
    }

    fun getDebtByCardRoom(idCard: Int): Flow<List<DebtsWalletEntity>> {
        return walletDao.getDebtsCardById(idCard)
    }

    suspend fun addDebtRoom(debt: DebtModel) {
        walletDao.addDebtCard(debt.toDebtsWalletEntity())
    }

    suspend fun updateDebtRoom(debt: DebtModel) {
        walletDao.updateDebtCard(debtCard = debt.toDebtsWalletEntity())
    }

    suspend fun deleteDebtRoom(debt: DebtModel) {
        walletDao.deleteDebtCard(debtCard = debt.toDebtsWalletEntity())
    }

    suspend fun deleteAllDebtRoom(idCard: Int) {
        walletDao.deleteAllDebtCard(idCard)
    }

}
