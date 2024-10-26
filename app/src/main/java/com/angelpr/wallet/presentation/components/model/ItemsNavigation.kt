package com.angelpr.wallet.presentation.components.model

import androidx.compose.ui.graphics.Color
import com.angelpr.wallet.R
import com.angelpr.wallet.presentation.navigation.ItemsNavScreen
import com.angelpr.wallet.ui.theme.Orange
import com.angelpr.wallet.utils.Constants

object ItemsNavigation{

    val list = listOf(
        NavigationItem(
            title = Constants.HOME,
            color = Orange,
            selectedIcon = R.drawable.icon_selected_home,
            unselectedIcon = R.drawable.icon_unselected_home,
            route = ItemsNavScreen.ScreenInit
        ),
        NavigationItem(
            title = Constants.DEBT,
            color = Color.Red,
            selectedIcon = R.drawable.icon_selected_debt,
            unselectedIcon = R.drawable.icon_unselected_debt,
            route = ItemsNavScreen.ScreenDebts
        ),
        NavigationItem(
            title = Constants.STATISTICS,
            color = Color.Cyan,
            selectedIcon = R.drawable.icon_selected_statistic,
            unselectedIcon = R.drawable.icon_unselected_statistic,
            route = ItemsNavScreen.ScreenStatistics
        ),
    )
}

data class NavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val color: Color,
    val route: ItemsNavScreen
)