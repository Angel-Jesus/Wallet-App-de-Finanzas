package com.angelpr.wallet.presentation.screen

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.ActionProcess
import com.angelpr.wallet.presentation.components.MessageDialog
import com.angelpr.wallet.presentation.components.NavigatorDrawer
import com.angelpr.wallet.presentation.components.WarningDialog
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.navigation.ItemsNavScreen
import com.angelpr.wallet.presentation.screen.tabs.DebtNotPaid
import com.angelpr.wallet.presentation.screen.tabs.DebtPaid
import com.angelpr.wallet.presentation.tab.ItemsTabScreen
import com.angelpr.wallet.presentation.tab.itemsTabScreens
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.GreenTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun DebtScreen(
    cardId: Int,
    viewModel: WalletViewModel = hiltViewModel(),
    drawerState: DrawerState,
    navController: NavController
) {
    val scope = rememberCoroutineScope()

    val uiCardState by viewModel.stateCard.collectAsState()
    val uiDebtState by viewModel.stateDebt.collectAsState()
    val debtTypeList by viewModel.totalDebtType.collectAsState()

    var emptyStateNotPaid by remember { mutableStateOf(true) }
    var emptyStatePaid by remember { mutableStateOf(true) }

    var enableButton by remember { mutableStateOf(false) }
    var cancelSchedule by remember { mutableStateOf(false) }

    var showWarning by remember { mutableStateOf(false) }
    val showPaidQuotaDialog = remember { mutableStateOf(false) }

    val indexDebt = remember { mutableIntStateOf(0) }
    var cardName by remember { mutableStateOf("No Card") }
    var dateExpired by remember { mutableLongStateOf(0L) }


    val pagerState = rememberPagerState(initialPage = 0, pageCount = { itemsTabScreens.size })

    LaunchedEffect(uiDebtState.state) {

        Log.i("DebtScreen", "state: ${uiDebtState.state.name}")

        if (uiDebtState.state == ActionProcess.SUCCESS || uiDebtState.state == ActionProcess.UPDATE_DEBT_BY_CARD) {
            viewModel.getDebtByCard(idCard = cardId, limit = 20)
        }
        if (uiDebtState.debtNotPaidList.isNotEmpty()) {
            if(uiDebtState.state == ActionProcess.DEBT_BY_CARD){
                emptyStateNotPaid = false
                viewModel.getDebtByType(uiDebtState.debtNotPaidList)
            }
        }
        else {
            emptyStateNotPaid = true
        }

        emptyStatePaid = !(uiDebtState.debtPaidList.isNotEmpty() && uiDebtState.state == ActionProcess.DEBT_BY_CARD)

        if(emptyStateNotPaid && cancelSchedule){
            Log.d("schedule", "cancel Schedule")
            cancelSchedule = false
            viewModel.cancelScheduleNotification(cardName, LocalDate.ofEpochDay(dateExpired))
        }
    }

    if (uiCardState.cardList.isNotEmpty()) {
        enableButton = true
    }

    if (showWarning) {
        WarningDialog(
            title = "Tener en cuenta",
            text = "Para añadir una deuda, es necesario tener una tarjeta guardada",
            onDismissRequest = { showWarning = false }
        )
    }

    if (showPaidQuotaDialog.value) {
        MessageDialog(
            onDismissRequest = { showPaidQuotaDialog.value = false },
            positiveButton = {
                // Update state of card's debt
                viewModel.updateDebtState(
                    id = uiDebtState.debtNotPaidList[indexDebt.intValue].id,
                    quotas = uiDebtState.debtNotPaidList[indexDebt.intValue].quotas,
                    quotasPaid = uiDebtState.debtNotPaidList[indexDebt.intValue].quotePaid + 1,
                    date = uiDebtState.debtNotPaidList[indexDebt.intValue].dateExpired
                )
                cardName = uiDebtState.debtNotPaidList[indexDebt.intValue].nameCard
                dateExpired = uiDebtState.debtNotPaidList[indexDebt.intValue].dateExpired

                showPaidQuotaDialog.value = false
                cancelSchedule = true
            },
            title = "Pago de cuota",
            text = "Si la quota es solo una se habrá pagado toda la deuda, en caso contrario, solo se registrará el pago de una quota"
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
                        if (enableButton) {
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
                    emptyStatePaid = emptyStatePaid,
                    emptyStateNotPaid = emptyStateNotPaid,
                    debtTypeList = debtTypeList,
                    uiDebtState = uiDebtState,
                    showPaidQuoteDialog = showPaidQuotaDialog,
                    indexDebt = indexDebt,
                    pagerState = pagerState
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
    emptyStatePaid: Boolean,
    emptyStateNotPaid: Boolean,
    debtTypeList: Map<String, Type>,
    uiDebtState: WalletViewModel.UiStateDebt,
    showPaidQuoteDialog: MutableState<Boolean>,
    indexDebt: MutableIntState,
    pagerState: PagerState
) {
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = pagerState,
    ) { page ->
        when (itemsTabScreens[page]) {
            ItemsTabScreen.DebtNotPaidTab ->
                DebtNotPaid(
                    emptyStateNotPaid = emptyStateNotPaid,
                    debtTypeList = debtTypeList,
                    uiDebtState = uiDebtState,
                    showPaidQuoteDialog = showPaidQuoteDialog,
                    indexDebt = indexDebt
                )

            ItemsTabScreen.DebtPaidTab ->
                DebtPaid(
                    emptyStatePaid = emptyStatePaid,
                    uiDebtState = uiDebtState,
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


