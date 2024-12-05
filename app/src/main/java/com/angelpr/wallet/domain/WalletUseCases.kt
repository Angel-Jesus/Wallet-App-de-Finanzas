package com.angelpr.wallet.domain

import com.angelpr.wallet.domain.wallet.AddWalletUseCase
import com.angelpr.wallet.domain.wallet.DeleteWalletUseCase
import com.angelpr.wallet.domain.wallet.GetWalletUseCase
import com.angelpr.wallet.domain.wallet.UpdateWalletUseCase

data class WalletUseCases(
    val getWallet: GetWalletUseCase,
    val addWallet: AddWalletUseCase,
    val updateWallet: UpdateWalletUseCase,
    val deleteWallet: DeleteWalletUseCase
)
