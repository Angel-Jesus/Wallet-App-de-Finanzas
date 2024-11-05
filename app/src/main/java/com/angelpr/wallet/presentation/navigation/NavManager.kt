package com.angelpr.wallet.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.angelpr.wallet.presentation.screen.AddDebtScreen
import com.angelpr.wallet.presentation.screen.AddEditWalletScreen
import com.angelpr.wallet.presentation.screen.DebtScreen
import com.angelpr.wallet.presentation.screen.ScreenInit
import com.angelpr.wallet.presentation.screen.StatisticsScreen
import com.angelpr.wallet.presentation.screen.event.DebtsEvent
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel

@SuppressLint("NewApi")
@Composable
fun NavManager(viewModel: WalletViewModel) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(key1 = true) {
        viewModel.getEnableNotification()
    }

    NavHost(
        navController = navController,
        startDestination = ItemsNavScreen.ScreenInit
    ) {
        composable<ItemsNavScreen.ScreenInit> {
            ScreenInit(
                viewModel = viewModel,
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<ItemsNavScreen.ScreenStatistics> {
            StatisticsScreen(
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<ItemsNavScreen.ScreenAddEditWallet> {
            val arg = it.toRoute<ItemsNavScreen.ScreenAddEditWallet>()
            AddEditWalletScreen(
                viewModel = viewModel,
                modeEdit = arg.modeEdit,
                navController = navController
            )
        }

        composable<ItemsNavScreen.ScreenDebts>{
            DebtScreen(
                viewModel = viewModel,
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<ItemsNavScreen.ScreenAddDebt>(
            enterTransition = {
                scaleIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    ),
                    initialScale = 0.2f
                )
            },
            exitTransition = {
                scaleOut(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    ),
                    targetScale = 1f
                )
            }
        ){
            AddDebtScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        /*
        composable<ItemsNavScreen.ScreenEditCard>{
            val args = it.toRoute<ItemsNavScreen.ScreenEditCard>()

            val card = CardModel(
                id = args.id,
                nameCard = args.nameWallet,
                creditLineCard = args.creditLine,
                typeMoney = args.typeMoney,
                paidDateExpired = args.dateExpiration.toInt(),
                dateClose = args.dateClose.toInt(),
                colorCard = args.colorCard.toULong()
            )

            EditCardScreen(
                cardModel = card,
                navController = navController,
            )
        }

         */
    }
}