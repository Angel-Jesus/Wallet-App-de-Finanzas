package com.angelpr.wallet.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.presentation.screen.AddDebtScreen
import com.angelpr.wallet.presentation.screen.AddWalletScreen
import com.angelpr.wallet.presentation.screen.DebtScreen
import com.angelpr.wallet.presentation.screen.EditCardScreen
import com.angelpr.wallet.presentation.screen.ScreenInit
import com.angelpr.wallet.presentation.screen.ScreenStatistics
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel

@SuppressLint("NewApi")
@Composable
fun NavManager(viewModel: WalletViewModel) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavHost(
        navController = navController,
        startDestination = AppScreens.ScreenInit
    ) {
        composable<AppScreens.ScreenInit> {
            ScreenInit(
                viewModel = viewModel,
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<AppScreens.ScreenStatistics> {
            ScreenStatistics(
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<AppScreens.ScreenAddWallet> {
            AddWalletScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<AppScreens.ScreenDebts>{
            DebtScreen(
                viewModel = viewModel,
                drawerState = drawerState,
                navController = navController
            )
        }

        composable<AppScreens.ScreenAddDebt>(
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

        composable<AppScreens.ScreenEditCard>{
            val args = it.toRoute<AppScreens.ScreenEditCard>()

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
                viewModel = viewModel,
                cardModel = card,
                navController = navController,
            )
        }
    }
}