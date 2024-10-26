package com.angelpr.wallet.presentation.screen

import android.icu.text.DecimalFormat
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.presentation.navigation.ItemsNavScreen
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.components.PieChart
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.ContainerColor
import com.angelpr.wallet.ui.theme.ContainerColorDark
import com.angelpr.wallet.ui.theme.ContainerInitDark
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScreenInit(
    indexCard: MutableIntState,
    cardId: MutableIntState,
    viewModel: WalletViewModel,
    drawerState: DrawerState,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    // Get uiState of Card
    val uiCardState by viewModel.stateCard.collectAsState()
    val lineUsedCard by viewModel.totalDebtCard.collectAsState()

    // Get uiState of Debt
    val uiDebtState by viewModel.stateDebt.collectAsState()
    val debtTypeList by viewModel.totalDebtType.collectAsState()

    var showDebtType by remember { mutableStateOf(false) }
    var colorContainer by remember { mutableStateOf(Color.Gray.value) }

    LaunchedEffect(Unit){
        viewModel.getAllCard()
    }

    // Check if indexCard is greater than last index update
    // The value to correct the possible error
    if(indexCard.intValue > uiCardState.cardList.lastIndex && indexCard.intValue != 0){
        indexCard.intValue -= 1
    }

    LaunchedEffect(key1 = uiCardState.state, key2 = indexCard.intValue) {
        if (uiCardState.cardList.isNotEmpty()) {
            // Save cardId to use in DebtScreen
            cardId.intValue = uiCardState.cardList[indexCard.intValue].id

            viewModel.getLineUseCard(
                cardId.intValue,
                uiCardState.cardList[indexCard.intValue].dateClose
            )
            viewModel.getDebtByCard(cardId.intValue)
        }
    }

    LaunchedEffect(uiDebtState.state) {
        if (uiDebtState.debtNotPaidList.isNotEmpty() && uiDebtState.state == ActionProcess.DEBT_BY_CARD) {
            showDebtType = true
            viewModel.getDebtByType(uiDebtState.debtNotPaidList)
        }else{
            showDebtType = false
        }
    }

    NavigatorDrawer(
        viewModel = viewModel,
        itemSelected = 0,
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    onDisplayDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            containerColor = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.background else ContainerColor
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                item {
                    TitleCards(navController, uiCardState, indexCard.intValue)
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if(isSystemInDarkTheme()) ContainerInitDark else Color.White
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

                            uiCardState.cardList.forEachIndexed { index, cardWallet ->

                                // Check which buttom is available
                                colorContainer = if (indexCard.intValue == index) {
                                    cardWallet.colorCard
                                } else {
                                    Color.Gray.value
                                }

                                CardWalletItem(colorContainer, cardWallet) {
                                    indexCard.intValue = index
                                }
                            }
                        }
                    }
                }

                if (uiCardState.cardList.isNotEmpty()) {
                    item {
                        // Show current balance
                        CurrentBalanceCard(
                            lineUsedCard = lineUsedCard,
                            card = uiCardState.cardList[indexCard.intValue]
                        )
                    }
                }

                if (showDebtType) {
                    item {
                        CardDebtType(debtTypeList)
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleCards(
    navController: NavController,
    uiState: WalletViewModel.UiStateCard,
    indexCard: Int
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isSystemInDarkTheme()) ContainerInitDark else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp),
                text = "Lista de cuentas",
                //color = Color.Black
            )

            OutlinedIconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = {
                    navController.navigate(ItemsNavScreen.ScreenAddWallet)
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Card",
                    tint = if(isSystemInDarkTheme()) Color.White else Color.Blue
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedIconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = {
                    // Pass parameter of card to editScreen
                    if (uiState.cardList.isNotEmpty()) {
                        navController.navigate(
                            ItemsNavScreen.ScreenEditCard(
                                id = uiState.cardList[indexCard].id,
                                nameWallet = uiState.cardList[indexCard].nameCard,
                                creditLine = uiState.cardList[indexCard].creditLineCard,
                                typeMoney = uiState.cardList[indexCard].typeMoney,
                                dateExpiration = uiState.cardList[indexCard].paidDateExpired.toString(),
                                dateClose = uiState.cardList[indexCard].dateClose.toString(),
                                colorCard = uiState.cardList[indexCard].colorCard.toLong()
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Add Card",
                    tint = if(isSystemInDarkTheme()) Color.White else Color.Blue
                )
            }

        }
    }
}

@Composable
fun CurrentBalanceCard(lineUsedCard: Float, card: CardModel?) {
    val formatter = DecimalFormat("#,###.00")

    val totalLine = card!!.creditLineCard.toFloat()

    val lineAvailable = totalLine - lineUsedCard
    val percentUsed = (lineUsedCard / totalLine)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if(isSystemInDarkTheme()) ContainerInitDark else Color.White
        )
    ) {
        Text(
            modifier = Modifier
                .padding(start = 14.dp, top = 8.dp),
            text = "Saldo Actual",
            //color = Color.Black
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            text = card.nameCard,
            //color = Color.Black,
            fontSize = 20.sp
        )

        LinearProgressIndicator(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp),
            progress = {
                percentUsed
            },
            color = Color(card.colorCard),
            trackColor = Color.LightGray

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
                text = card.typeMoney + " " + formatter.format(lineUsedCard),
                //color = Color.Black
            )

            Text(
                modifier = Modifier
                    .padding(end = 14.dp),
                text = card.typeMoney + " " + formatter.format(lineAvailable),
                //color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp),
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
        Spacer(modifier = Modifier.height(8.dp))
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
private fun CardDebtType(data: Map<String, Type>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if(isSystemInDarkTheme()) ContainerInitDark else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.TopStart),
                text = "Estructura de deuda",
                fontWeight = FontWeight.Bold,
                //color = Color.Black
            )

            PieChart(
                //color = Color.Black,
                data = data
            )

            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTitleCards() {
    MaterialTheme {
        CardDebtType(
            data = mapOf(
                "Compras" to Categories.Debt[0].copy(value = 30.02f)
            )
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    onDisplayDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Inicio")
        },
        navigationIcon = {
            IconButton(
                onClick = onDisplayDrawer
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






