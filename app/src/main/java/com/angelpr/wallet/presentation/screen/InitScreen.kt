package com.angelpr.wallet.presentation.screen

import android.icu.text.DecimalFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.navigation.AppScreens
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.ContainerColor
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScreenInit(
    viewModel: WalletViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavController
) {

    val uiState by viewModel.stateCard.collectAsState()
    var colorContainer by remember { mutableStateOf(Color.Gray.value) }

    LaunchedEffect(scope) {
        viewModel.getAllCard()
        //Log.i("cardList", uiState.cardList.toString())
    }

    var indexCard by remember { mutableIntStateOf(0) }

    NavigatorDrawer(
        itemSelected = 0,
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(scope, drawerState)
            },
            containerColor = ContainerColor
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 16.dp),
                                text = "Lista de cuentas",
                                color = Color.Black
                            )

                            OutlinedIconButton(
                                modifier = Modifier
                                    .size(32.dp),
                                onClick = {
                                    // Pass parameter of card to editScreen
                                    navController.navigate(
                                        AppScreens.ScreenEditCard(
                                            id = uiState.cardList[indexCard].id,
                                            nameWallet = uiState.cardList[indexCard].nameCard,
                                            creditLine = uiState.cardList[indexCard].creditLineCard,
                                            typeMoney = uiState.cardList[indexCard].typeMoney,
                                            dateExpiration = uiState.cardList[indexCard].paidDateExpired.toString(),
                                            dateClose = uiState.cardList[indexCard].dateClose.toString(),
                                            colorCard = uiState.cardList[indexCard].colorCard.toLong()
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Add Card",
                                    tint = Color.Blue
                                )
                            }

                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            maxItemsInEachRow = 2
                        ) {

                            uiState.cardList.forEachIndexed { index, cardWallet ->

                                // Check which buttom is available
                                colorContainer = if(indexCard == index){
                                    cardWallet.colorCard
                                }else{
                                    Color.Gray.value
                                }

                                CardWalletItem(colorContainer, cardWallet) {
                                    indexCard = index
                                }
                            }

                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(0.49f),
                                onClick = { navController.navigate(AppScreens.ScreenAddWallet) },
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 8.dp, top = 10.dp, bottom = 10.dp),
                                    text = "AÑADIR CUENTA"
                                )
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Rounded.AddCircle,
                                    contentDescription = "Add Card"
                                )
                            }
                        }
                    }
                }

                if (uiState.cardList.isNotEmpty()) {
                    item {
                        CurrentBalanceCard(card = uiState.cardList[indexCard])
                    }
                }

            }
        }
    }
}

@Composable
fun CurrentBalanceCard(card: CardModel?) {
    val formatter = DecimalFormat("#,###.00")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            modifier = Modifier
                .padding(start = 14.dp, top = 8.dp),
            text = "Saldo Actual",
            color = Color.Black
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            text = card?.nameCard ?: "No hay Tarjeta",
            fontSize = 20.sp
        )

        LinearProgressIndicator(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp),
            progress = {
                0.6f
            },
            color = Color(card?.colorCard ?: Color.Blue.value)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 14.dp),
                text = "0"
            )

            Text(
                modifier = Modifier
                    .padding(end = 14.dp),
                text = (card?.typeMoney
                    ?: "PEN") + " " + formatter.format(card?.creditLineCard?.toDouble() ?: 0)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 14.dp),
                text = "Linea utilizada",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                modifier = Modifier
                    .padding(end = 14.dp),
                text = "Linea disponible",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CardWalletItem(
    colorContainer: ULong,
    card: CardModel,
    onClick: () -> Unit
) {

    val formatter = DecimalFormat("#,###.00")

    Card(
        modifier = Modifier
            .fillMaxWidth(0.49f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(colorContainer)
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp),
            text = card.nameCard,
            fontSize = 12.sp,
            color = Color.LightGray
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 4.dp),
            text = card.typeMoney + " " + formatter.format(card.creditLineCard.toDouble()),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = {
            Text(text = "Inicio")
        },
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
                    contentDescription = "Menu Screen Inicio"
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
fun PreviewCard() {
    MaterialTheme {
        CurrentBalanceCard(
            card = CardModel(
                nameCard = "Bbva BeFree",
                creditLineCard = "1000",
                typeMoney = "PEN",

                colorCard = Color.DarkGray.value
            )
        )
    }
}
 */

/*
@Preview(showBackground = true)
@Composable
fun ScreenInitPreview(){

    MaterialTheme {
        ScreenInit(
            scope = rememberCoroutineScope(),
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            navController = rememberNavController()
        )
    }
}

 */




