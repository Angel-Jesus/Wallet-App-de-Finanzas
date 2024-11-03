package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.utils.getMonthInCase
import java.time.LocalDate
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend fun Card(cardModel: CardModel): ActionProcess =
        repository.updateCardToDatabase(cardModel)

    suspend fun DebtState(id: Int, quotas: Int, quotasPaid: Int, date: Long): ActionProcess =
        repository.updateDebtToDatabase(
            id = id,
            quotas = quotas,
            quotasPaid = quotasPaid,
            dateExpired = date
        )

}