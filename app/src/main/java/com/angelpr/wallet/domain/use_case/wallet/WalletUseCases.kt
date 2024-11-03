package com.angelpr.wallet.domain.use_case.wallet

data class WalletUseCases(
    val getWallet: GetWalletUseCase,
    val addWallet: AddWalletUseCase,
    val updateWallet: UpdateWalletUseCase,
    val deleteWallet: DeleteWalletUseCase
)
