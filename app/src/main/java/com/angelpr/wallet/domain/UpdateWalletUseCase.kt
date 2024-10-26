package com.angelpr.wallet.domain

import com.angelpr.wallet.data.WalletRepository
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import java.time.LocalDate
import javax.inject.Inject

class UpdateWalletUseCase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    suspend fun Card(cardModel: CardModel): ActionProcess =
        walletRepository.updateCardToDatabase(cardModel)

    suspend fun DebtState(id: Int, quotas: Int, quotasPaid: Int, dateExpired: Long): ActionProcess {
        val plusDateExpired = LocalDate.ofEpochDay(dateExpired).plusMonths(1)

        return if (quotasPaid == quotas) {
            walletRepository.updateDebtToDatabase(id, quotasPaid, 1, dateExpired = dateExpired)
        } else {
            walletRepository.updateDebtToDatabase(id, quotasPaid, 0, dateExpired = plusDateExpired.toEpochDay())
        }
    }

}