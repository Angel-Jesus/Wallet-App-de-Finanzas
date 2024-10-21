package com.angelpr.wallet.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.angelpr.wallet.R
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.navigation.AppScreens
import com.angelpr.wallet.presentation.components.EmptyStateScreen
import com.angelpr.wallet.presentation.components.ErrorDialog
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.components.PieChart
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.GreenTopBar
import com.angelpr.wallet.ui.theme.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DebtScreen(
    cardId: Int,
    viewModel: WalletViewModel,
    drawerState: DrawerState,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val uiCardState by viewModel.stateCard.collectAsState()
    val uiDebtState by viewModel.stateDebt.collectAsState()
    val debtTypeList by viewModel.totalDebtType.collectAsState()

    var enable by remember { mutableStateOf(false) }
    var enableButton by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }

    LaunchedEffect(scope) {
        viewModel.getAllCard()
        viewModel.getDebtByCard(cardId)
    }

    LaunchedEffect(uiDebtState) {
        if (uiDebtState.debtList.isNotEmpty()) {
            enable = true
            viewModel.getDebtByType(uiDebtState.debtList)
        }
    }

    if(uiCardState.cardList.isNotEmpty()){
        enableButton = true
    }

    if (showWarning) {
        ErrorDialog(
            title = "Tener en cuenta",
            text = "Para añadir una tarjeta, es necesario tener una tarjeta guardada",
            onDismissRequest = { showWarning = false }
        )
    }

    NavigatorDrawer(
        itemSelected = 1,
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(scope, drawerState)
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp),
                    onClick = {
                        if (enableButton) {
                            navController.navigate(AppScreens.ScreenAddDebt)
                        } else {
                            showWarning = true
                        }
                    },
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
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize(align = if(enable) { Alignment.TopStart } else {Alignment.Center})
            ) {
                if (enable) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(270.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (enable) {
                                PieChart(
                                    data = debtTypeList
                                )
                            }

                        }

                    }

                    item {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 8.dp),
                            text = "Deudas pendientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(uiDebtState.debtList) { debtModel ->
                        CardDebt(debtModel)
                    }
                } else {
                    item {
                        EmptyStateScreen(
                            title = "Nada registrado",
                            text = "No tiene deuda pendiente o registrada"
                        )
                    }
                }

            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun CardDebt(debtModel: DebtModel) {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.ofEpochDay(debtModel.date)
    val dateExpired = LocalDate.ofEpochDay(debtModel.dateExpired)

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 14.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_wallet),
                    contentDescription = "Icon Wallet",
                    tint = Wallet
                )

                Text(
                    text = debtModel.nameCard,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )

                Text(
                    text = date.format(formatter),
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                val category = getCategory(debtModel.type)
                Icon(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp),
                    imageVector = ImageVector.vectorResource(category.icon),
                    contentDescription = category.name,
                    tint = category.color
                )

                Text(
                    text = debtModel.type,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                Text(
                    text = debtModel.typeMoney + " " + debtModel.debt.toString(),
                    color = Color.Gray
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    text = "Fecha de pago: ${dateExpired.format(formatter)}",
                    color = Color.Gray
                )


                Text(
                    text = if (debtModel.quotas > 1) {
                        "Cuotas: ${debtModel.quotePaid}/${debtModel.quotas}"
                    } else {
                        "Directo"
                    },
                    color = Color.Gray
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }

    }
}

private fun getCategory(category: String): Type = when (category) {
    Categories.Debt[0].name -> Categories.Debt[0]
    Categories.Debt[1].name -> Categories.Debt[1]
    Categories.Debt[2].name -> Categories.Debt[2]
    Categories.Debt[3].name -> Categories.Debt[3]
    else -> Categories.Debt[4]
}


@SuppressLint("NewApi")
@Preview(showBackground = true)
@Composable
fun DebtCardPreview() {
    MaterialTheme {
        CardDebt(
            DebtModel(
                idWallet = 1,
                nameCard = "Bbva Befree",
                typeMoney = "PEN",
                debt = 12.5f,
                type = Categories.Debt[0].name,
                quotePaid = 0,
                quotas = 0,
                isPaid = 0,
                date = LocalDate.of(2024, 10, 18).toEpochDay(),
                dateExpired = LocalDate.of(2024, 12, 5).toEpochDay()
            )
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
            drawerState = rememberDrawerState(DrawerValue.Closed),
            navController = rememberNavController()
        )
    }
}

 */

