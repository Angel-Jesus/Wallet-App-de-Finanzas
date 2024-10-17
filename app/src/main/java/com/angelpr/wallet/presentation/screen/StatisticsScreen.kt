package com.angelpr.wallet.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScreenStatistics(
    drawerState: DrawerState,
    navController: NavController) {

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
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                item {
                    Text(
                        text = "Hello Screen Stadistics",
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
        title = { Text(text = "Estadistica") },
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
                    contentDescription = "Menu Screen Statistics"
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