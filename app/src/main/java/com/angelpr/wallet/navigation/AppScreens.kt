package com.angelpr.wallet.navigation

import kotlinx.serialization.Serializable

sealed class AppScreens {
    @Serializable
    data object ScreenInit: AppScreens()
    @Serializable
    data object ScreenStatistics: AppScreens()
    @Serializable
    data object ScreenAddWallet: AppScreens()
    @Serializable
    data object ScreenDebts: AppScreens()
    @Serializable
    data class ScreenEditCard(
        val id: Int,
        val nameWallet: String,
        val creditLine: String,
        val typeMoney: String,
        val dateExpiration: String,
        val dateClose: String,
        val colorCard: Long
    ): AppScreens()
    /*
    @Serializable
    data object ScreenSettings: AppScreens()
     */
}