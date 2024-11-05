package com.angelpr.wallet.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.MessageDialog
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.components.WarningDialog
import com.angelpr.wallet.presentation.navigation.ItemsNavScreen
import com.angelpr.wallet.presentation.screen.event.DebtsEvent
import com.angelpr.wallet.presentation.screen.tabs.DebtNotPaid
import com.angelpr.wallet.presentation.screen.tabs.DebtPaid
import com.angelpr.wallet.presentation.tab.ItemsTabScreen
import com.angelpr.wallet.presentation.tab.itemsTabScreens
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
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { itemsTabScreens.size })

    val uiCardState by viewModel.stateCard.collectAsState()
    val uiDebtState by viewModel.stateDebt.collectAsState()

    var debtItem by remember { mutableStateOf<DebtModel?>(null) }

    var cancelSchedule by remember { mutableStateOf(false) }
    var showWarning by remember { mutableStateOf(false) }
    var showDebtDialog by remember { mutableStateOf(false) }
    var isNotDebtPaid by remember { mutableStateOf(false) }

    if(uiDebtState.debtNotPaidList.none { (debtItem?.dateExpired ?: -1) == it.dateExpired } && cancelSchedule){
        cancelSchedule = false
        viewModel.cancelScheduleNotification(debtItem!!.nameCard, listOf(debtItem!!.dateExpired))
    }

    if (showWarning) {
        WarningDialog(
            title = "Tener en cuenta",
            text = "Para añadir una deuda, es necesario tener una tarjeta guardada",
            onDismissRequest = { showWarning = false }
        )
    }

    if (showDebtDialog) {
        MessageDialog(
            onDismissRequest = { showDebtDialog = false },
            positiveButton = {
                if (isNotDebtPaid) {
                    viewModel.onEventDebt(
                        DebtsEvent.UpdateDebts(
                            debtItem!!.copy(quotePaid = debtItem!!.quotePaid + 1)
                        )
                    )
                    cancelSchedule = true
                } else {
                    viewModel.onEventDebt(DebtsEvent.DeleteDebt(debtItem!!))
                }
                showDebtDialog = false
            },
            negativeButton = {
                if (isNotDebtPaid) {
                    viewModel.onEventDebt(DebtsEvent.DeleteDebt(debtItem!!))
                }
                showDebtDialog = false
            },
            textNegative = if (isNotDebtPaid) "Eliminar" else "Cancelar",
            textPositive = if (isNotDebtPaid) "Confirmar" else "Eliminar",
            title = if (isNotDebtPaid) "Pago de cuota o eliminar deuda" else "Eliminar pago",
            text = if (isNotDebtPaid) "Si la quota es solo una se habrá pagado toda la deuda, en caso contrario, solo se registrará el pago de una quota" else "Esta por eliminar un pago del registro"
        )
    }

    NavigatorDrawer(
        viewModel = viewModel,
        itemSelected = 1,
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp),
                    onClick = {
                        if (uiCardState.cardList.isNotEmpty()) {
                            navController.navigate(ItemsNavScreen.ScreenAddDebt)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Tabs(
                    scope = scope,
                    pagerState = pagerState
                )
                TabsContent(
                    pagerState = pagerState,
                    uiDebtState = uiDebtState,
                    onDebtPaidClick = { debtPaid ->
                        debtItem = debtPaid
                        isNotDebtPaid = false
                        showDebtDialog = true
                    },
                    onDebtNotPaidClick = { debtNotPaid ->
                        debtItem = debtNotPaid
                        isNotDebtPaid = true
                        showDebtDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun Tabs(
    scope: CoroutineScope,
    pagerState: PagerState
) {
    val selectedTab = pagerState.currentPage
    TabRow(
        modifier = Modifier
            .fillMaxWidth(),
        indicator = { tabPositions ->
            if (selectedTab < tabPositions.size) {

                val gradient = Brush.verticalGradient(
                    0.0f to Color.White,
                    0.9f to Color.LightGray,
                    1f to Color.LightGray
                )

                if (selectedTab < tabPositions.size) {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(gradient)
                    )
                }
            }
        },
        selectedTabIndex = selectedTab,
        contentColor = Color.White,
        containerColor = GreenTopBar,
    ) {
        itemsTabScreens.forEachIndexed { index, item ->
            Tab(
                selected = selectedTab == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(text = item.title) }
            )
        }
    }
}

@Composable
fun TabsContent(
    uiDebtState: WalletViewModel.UiStateDebt,
    pagerState: PagerState,
    onDebtNotPaidClick: (DebtModel) -> Unit,
    onDebtPaidClick: (DebtModel) -> Unit,
) {
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = pagerState,
    ) { page ->
        when (itemsTabScreens[page]) {
            ItemsTabScreen.DebtNotPaidTab ->
                DebtNotPaid(
                    uiDebtState = uiDebtState,
                    onClick = { onDebtNotPaidClick(it) }
                )

            ItemsTabScreen.DebtPaidTab ->
                DebtPaid(
                    uiDebtState = uiDebtState,
                    oncClick = { onDebtPaidClick(it) }
                )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    onOpenDrawer: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Deudas") },
        navigationIcon = {
            IconButton(
                onClick = onOpenDrawer
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


@Preview(showBackground = true)
@Composable
fun TabsPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                onOpenDrawer = {}
            )
            /*
            Tabs(
                scope = rememberCoroutineScope(),
                pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { itemsTabScreens.size })
            )
             */
        }

    }
}


