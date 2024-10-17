package com.angelpr.wallet.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.angelpr.wallet.navigation.AppScreens
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DebtScreen(
    viewModel: WalletViewModel,
    drawerState: DrawerState,
    navController: NavController
) {

    val scope = rememberCoroutineScope()

    NavigatorDrawer(
        itemSelected = 1,
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(scope, drawerState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp),
                    onClick = { navController.navigate(AppScreens.ScreenAddDebt) },
                    containerColor = GreenTopBar,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add debts"
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    Text(
                        text = "Hello Screen Debts",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = { Text(text = "Deudas") },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Screen Debts"
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = GreenTopBar,
            scrolledContainerColor = GreenTopBar,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = GreenTopBar
        )
    )
}

/*
@Preview(showBackground = true)
@Composable
fun previewDebtScreen() {
    MaterialTheme {
        DebtScreen(
            scope = rememberCoroutineScope(),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            navController = rememberNavController()
        )
    }
}
 */

