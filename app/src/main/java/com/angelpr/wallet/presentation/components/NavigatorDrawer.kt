package com.angelpr.wallet.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.angelpr.wallet.R
import com.angelpr.wallet.presentation.components.model.ItemsNavigation
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.ContainerColorDark
import com.angelpr.wallet.ui.theme.ContainerInitDark
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigatorDrawer(
    viewModel: WalletViewModel,
    itemSelected: Int = 0,
    scope: CoroutineScope,
    navController: NavController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {

    val enableNotifications by viewModel.enableNotification.collectAsState()

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
                    ) {
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
                                painter = painterResource(id = R.drawable.logo_wallet),
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
                            .background(if(isSystemInDarkTheme()) ContainerColorDark  else Color.White)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        ItemsNavigation.list.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        modifier = Modifier
                                            .padding(start = 8.dp),
                                        text = item.title,
                                        color = if(isSystemInDarkTheme()) Color.White else Color.Black
                                    )
                                },
                                selected = index == itemSelected,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    if (index != itemSelected) {
                                        navController.navigate(item.route)
                                    }
                                },
                                icon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp),
                                        imageVector = if (index == itemSelected) {
                                            ImageVector.vectorResource(item.selectedIcon)
                                        } else {
                                            ImageVector.vectorResource(item.unselectedIcon)
                                        },
                                        tint = item.color,
                                        contentDescription = item.title
                                    )
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = if(isSystemInDarkTheme()) ContainerInitDark else Color.LightGray,
                                ),
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(1.dp)
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 8.dp),
                            text = "Configuracion",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Row(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 8.dp)
                                .fillMaxWidth()
                        ){
                            Column {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 4.dp),
                                    text = "Notificaciones",
                                    //color = Color.Black
                                )
                                Text(
                                    modifier = Modifier
                                        .width(250.dp),
                                    text = "Recibe una notificacion sobre tus deudas pendientes una semana antes de tu fecha de pago",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Justify
                                )
                            }

                            Switch(
                                modifier = Modifier
                                    .scale(0.8f)
                                    .weight(1f),
                                checked = enableNotifications,
                                onCheckedChange = { valueChange ->
                                    viewModel.updateEnableNotification(valueChange)
                                }
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

