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

class WalletRepositoryImpl(
    private val walletDao: WalletDao
) : WalletRepository {

    // Action by Cards
    override suspend fun getAllCardRoom(): List<CardModel> {
        val response: List<CardWalletEntity> = walletDao.getAllCards()
        return if (response.isNotEmpty()) {
            response.map { it.toCardWallet() }
        } else {
            emptyList()
        }
    }

    override suspend fun addCardRoom(card: CardModel) {
        walletDao.insertCard(card.toCardWalletEntity())
    }

    override suspend fun updateCardRoom(card: CardModel) {
        walletDao.updateCard(card.toCardWalletEntity())
    }

    override suspend fun deleteCardRoom(id: Int) {
        walletDao.deleteCard(id)
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

    override suspend fun getDebtNotPaidTRoom(idCard: Int): List<DebtModel> {
        val response: List<DebtsWalletEntity> = walletDao.getDebtNotPaidCardById(idCard = idCard)

        return if (response.isNotEmpty()) {
            return response.map { it.toDebtsWallet() }
        } else {
            emptyList()
        }
    }

    override suspend fun getDebtPaidRoom(idCard: Int, limit: Int): List<DebtModel> {
        val response: List<DebtsWalletEntity> =
            walletDao.getDebtPaidCardById(idCard = idCard, limit = limit)
        return if (response.isNotEmpty()) {
            response.map { it.toDebtsWallet() }
        } else {
            emptyList()
        }
    }

    override suspend fun addDebtRoom(debt: DebtModel) {
        walletDao.addDebtCard(debt.toDebtsWalletEntity())
    }

    override suspend fun updateDebtRoom(debt: DebtModel) {
        walletDao.updateDebtCard(debtCard = debt.toDebtsWalletEntity())
    }

    override suspend fun deleteDebtRoom(id: Int) {
        walletDao.deleteDebtCard(id)
    }

    override suspend fun deleteAllDebtRoom(idCard: Int) {
        walletDao.deleteAllDebtCard(idCard)
    }

}

/*
override suspend fun updateDebtRoom(
        id: Int,
        quotas: Int,
        quotasPaid: Int,
        dateExpired: Long
    ): ActionProcess {

        val isPaid = if (quotasPaid == quotas) 1 else 0
        val date = getMonthInCase(dateExpired, quotas == quotasPaid)

        walletDao.updateDebtCard(debtCard = )
        return ActionProcess.UPDATE_DEBT_BY_CARD
    }
 */