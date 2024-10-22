package com.angelpr.wallet.presentation.tab

sealed class ItemsTabScreen(
    val title: String,
) {
    data object DebtNotPaidTab: ItemsTabScreen(
        title = "Pendientes"
    )
    data object DebtPaidTab: ItemsTabScreen(
        title = "Canceladas"
    )
}

val itemsTabScreens = listOf(
    ItemsTabScreen.DebtNotPaidTab,
    ItemsTabScreen.DebtPaidTab
)