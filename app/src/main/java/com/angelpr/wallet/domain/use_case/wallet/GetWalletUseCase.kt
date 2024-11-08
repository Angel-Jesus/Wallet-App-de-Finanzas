package com.angelpr.wallet.domain.use_case.wallet

import com.angelpr.wallet.data.db.entities.toCardWallet
import com.angelpr.wallet.data.db.entities.toDebtsWallet
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.domain.repository.WalletRepository
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.utils.getInitDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetWalletUseCase(
    private val repository: WalletRepository
) {

    fun allCard(): Flow<List<CardModel>> = repository.getCardsRoom().map{cards ->
        cards.map { it.toCardWallet() }
    }

    fun getDebtCard(idCard: Int): Flow<List<DebtModel>> = repository.getDebtByCardRoom(idCard).map { debts ->
        debts.map { it.toDebtsWallet() }
    }

    fun getTotalDebtType(debtList: List<DebtModel>): Map<String, Type> =
        repository.getTotalDebtType(debtList)

    suspend fun getLineUseCard(card: CardModel): Float {

        val init = getInitDate(card.dateClose)
        val end = init.plusMonths(1)

        val initDate = init.toEpochDay()
        val endDate = end.toEpochDay()

        return repository.getLineUseRoom(idCard = card.id, dateInit = initDate, dateEnd = endDate)
    }

}