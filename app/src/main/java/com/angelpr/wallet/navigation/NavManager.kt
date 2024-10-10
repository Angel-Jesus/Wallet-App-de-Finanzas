package com.angelpr.wallet.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.angelpr.wallet.presentation.view.ScreenInit
import com.angelpr.wallet.presentation.view.ScreenStatistics

@Composable
fun NavManager(){

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavHost(
        navController = navController,
        startDestination = AppScreens.ScreenInit
    ){
        composable<AppScreens.ScreenInit>{
            ScreenInit(scope = scope, drawerState = drawerState, navController = navController)
        }

        composable<AppScreens.ScreenStatistics>{
            ScreenStatistics(scope = scope, drawerState = drawerState, navController = navController)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun ScreenInitPreview() {
    MaterialTheme {
        NavManager()
    }
}