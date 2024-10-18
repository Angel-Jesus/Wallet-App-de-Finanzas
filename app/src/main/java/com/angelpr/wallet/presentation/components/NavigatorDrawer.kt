package com.angelpr.wallet.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.angelpr.wallet.R
import com.angelpr.wallet.navigation.AppScreens
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigatorDrawer(
    itemSelected: Int = 0,
    scope: CoroutineScope,
    navController: NavController,
    drawerState: DrawerState,
    content: @Composable () -> Unit) {

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = GreenTopBar,
                    drawerShape = RoundedCornerShape(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 115.dp, max = 130.dp)
                            .background(GreenTopBar),
                        contentAlignment = Alignment.CenterStart
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp)
                                    .height(60.dp)
                                    .fillMaxWidth(0.2f),
                                painter = painterResource(id = R.drawable.wallet),
                                contentDescription = "Logo App"
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Wallet: App de finanzas",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.White)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        items.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = { Text(text = item.title) },
                                selected = index == itemSelected,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    if(index != itemSelected){
                                        navController.navigate(item.route)
                                    }

                                },
                                icon = {
                                    Icon(
                                        imageVector = if(index == itemSelected) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.title
                                    )
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }


                }
            },
            drawerState = drawerState
        ) {
            content()
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: AppScreens
)

val items = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = AppScreens.ScreenInit
    ),
    NavigationItem(
        title = "Deudas",
        selectedIcon = Icons.Filled.Delete,
        unselectedIcon = Icons.Outlined.Delete,
        route = AppScreens.ScreenDebts
    ),
    NavigationItem(
        title = "Estadistica",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        route = AppScreens.ScreenStatistics
    ),
    /*
    NavigationItem(
        title = "Configuracion",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = TODO()
    ),

 */
)

@Preview(showBackground = true)
@Composable
fun NavigatorDrawerPreview() {
    MaterialTheme {
        NavigatorDrawer(
            itemSelected = 0,
            scope = rememberCoroutineScope(),
            navController = rememberNavController(),
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        ){

        }
    }
}