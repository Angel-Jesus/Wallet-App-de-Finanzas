package com.angelpr.wallet.navigation

import kotlinx.serialization.Serializable

sealed class AppScreens {
    @Serializable
    data object ScreenInit: AppScreens()
    @Serializable
    data object ScreenStatistics: AppScreens()

    /*
    @Serializable
    data object ScreenDebts: AppScreens()
    @Serializable
    data object ScreenSettings: AppScreens()
     */
}