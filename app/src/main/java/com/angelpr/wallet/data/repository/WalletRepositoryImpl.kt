package com.angelpr.wallet.data.repository

import com.angelpr.wallet.data.db.dao.WalletDao
import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity
import com.angelpr.wallet.data.db.entities.toCardWallet
import com.angelpr.wallet.data.db.entities.toCardWalletEntity
import com.angelpr.wallet.data.db.entities.toDebtsWallet
import com.angelpr.wallet.data.db.entities.toDebtsWalletEntity
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.utils.sumOfFloat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WalletRepositoryImpl(
    private val walletDao: WalletDao
) : WalletRepository {

    // Action by Cards
    override fun getCardsRoom(): Flow<List<CardWalletEntity>> {
        return walletDao.getCards()
    }

    override suspend fun addCardRoom(card: CardModel) {
        walletDao.insertCard(card.toCardWalletEntity())
    }

    override suspend fun updateCardRoom(card: CardModel) {
        walletDao.updateCard(card.toCardWalletEntity())
    }

    override suspend fun deleteCardRoom(card: CardModel) {
        walletDao.deleteCard(card.toCardWalletEntity())
    }

    // Action by Debts
    override fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type> {
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

    override suspend fun getLineUseRoom(idCard: Int, dateInit: Long, dateEnd: Long): Float {
        return walletDao.getLineUseCard(idCard = idCard, dateInit = dateInit, dateEnd = dateEnd)
    }

    override fun getDebtByCardRoom(idCard: Int): Flow<List<DebtsWalletEntity>> {
        return walletDao.getDebtsCardById(idCard)
    }

    override suspend fun addDebtRoom(debt: DebtModel) {
        walletDao.addDebtCard(debt.toDebtsWalletEntity())
    }

    override suspend fun updateDebtRoom(debt: DebtModel) {
        walletDao.updateDebtCard(debtCard = debt.toDebtsWalletEntity())
    }

    override suspend fun deleteDebtRoom(debt: DebtModel) {
        walletDao.deleteDebtCard(debtCard = debt.toDebtsWalletEntity())
    }

    override suspend fun deleteAllDebtRoom(idCard: Int) {
        walletDao.deleteAllDebtCard(idCard)
    }

}
