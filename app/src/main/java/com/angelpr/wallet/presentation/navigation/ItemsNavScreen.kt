package com.angelpr.wallet.presentation.navigation

import kotlinx.serialization.Serializable

sealed class ItemsNavScreen {
    @Serializable
    data object ScreenInit: ItemsNavScreen()
    @Serializable
    data object ScreenStatistics: ItemsNavScreen()
    @Serializable
    data object ScreenAddWallet: ItemsNavScreen()
    @Serializable
    data object ScreenDebts: ItemsNavScreen()
    @Serializable
    data class ScreenEditCard(
        val id: Int,
        val nameWallet: String,
        val creditLine: String,
        val typeMoney: String,
        val dateExpiration: String,
        val dateClose: String,
        val colorCard: Long
    ): ItemsNavScreen()
    @Serializable
    data object ScreenAddDebt: ItemsNavScreen()
/*
    @Serializable
    data object ScreenSettings: ItemsNavScreen()
 */

}