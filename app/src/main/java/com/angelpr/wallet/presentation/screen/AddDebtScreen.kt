package com.angelpr.wallet.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angelpr.wallet.R
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.screen.event.DebtsEvent
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.GreenTopBar
import com.angelpr.wallet.ui.theme.Wallet
import com.angelpr.wallet.utils.getDateExpired
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@SuppressLint("NewApi")
@Composable
fun AddDebtScreen(
    viewModel: WalletViewModel,
    navController: NavController
) {

    val uiCardState by viewModel.stateCard.collectAsState()
    val enableNotifications by viewModel.enableNotification.collectAsState()

    var cardItem by remember { mutableStateOf(uiCardState.cardList[0]) }

    var cost by remember { mutableStateOf("") }
    var quotas by remember { mutableStateOf("1") }
    var category by remember { mutableStateOf(Categories.Debt[0].name) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlowAddEditDebt.collectLatest { event ->
            when (event) {
                WalletViewModel.UiEvent.Success -> navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                onBack = { navController.popBackStack() },
                onSaveData = {
                    val dateExpired =
                        getDateExpired(cardItem.paidDateExpired, cardItem.dateClose)

                    // Add schedule Notification
                    // Add conditional to enable or disable option to schedule notification
                    // It's depende on the state of the switch button
                    // In case there are notification with the same id, the notification will be updated
                    if (enableNotifications) {
                        viewModel.setScheduleNotification(
                            cardName = cardItem.nameCard,
                            daysToSubtract = 7,
                            dateExpired = LocalDate.ofEpochDay(dateExpired)
                        )
                    }
                    // Add debt
                    viewModel.onEventDebt(
                        DebtsEvent.AddDebts(
                            DebtModel(
                                idWallet = cardItem.id,
                                nameCard = cardItem.nameCard,
                                typeMoney = cardItem.typeMoney,
                                debt = cost.toFloat(),
                                type = category,
                                isPaid = 0,
                                quotas = quotas.toInt(),
                                date = LocalDate.now().toEpochDay(),
                                dateExpired = dateExpired,
                            )
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Cuenta",
                color = Color.Gray,
                fontSize = 13.sp
            )

            DropDownCard(
                paddingStart = 8.dp,
                paddingEnd = 8.dp,
                paddingTop = 8.dp,
                listCards = uiCardState.cardList
            ) { card ->
                cardItem = card
            }

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Monto",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { isFocused ->
                        if (isFocused.isFocused) {
                            if (cost == "0") {
                                cost = ""
                            }
                        } else {
                            if (cost == "") {
                                cost = "0"
                            }
                        }
                    },
                value = cost,
                onValueChange = { newValue ->
                    cost = newValue
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Cuotas",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { isFocused ->
                        if (isFocused.isFocused) {
                            if (quotas == "1") {
                                quotas = ""
                            }
                        } else {
                            if (quotas == "") {
                                quotas = "1"
                            }
                        }
                    },
                value = quotas,
                onValueChange = { newValue ->
                    quotas = newValue
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Categoria",
                color = Color.Gray,
                fontSize = 13.sp
            )

            DropDownCategory(
                paddingStart = 8.dp,
                paddingEnd = 8.dp,
                paddingTop = 8.dp,
            ) { type ->
                category = type.name
            }

        }
    }
}

@Composable
private fun DropDownCategory(
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    typeSelected: (Type) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(Categories.Debt[0]) }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
                bottom = paddingBottom
            )
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            enabled = false,
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Icon(
                        modifier = Modifier
                            .drawBehind {
                                drawCircle(
                                    color = category.color,
                                    radius = this.size.maxDimension - 4f
                                )
                            }
                            .size(18.dp),
                        imageVector = ImageVector.vectorResource(category.icon),
                        contentDescription = category.name,
                        tint = Color.White
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 14.dp),
                        text = category.name,
                        color = Color.Black
                    )
                }
            },
            trailingIcon = {
                Icon(
                    icon,
                    "Icon Filter",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = Color.Black
                )
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.LightGray
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(textFiledSize.width.dp)
                .offset(y = 0.dp),
        ) {
            Categories.Debt.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            color = type.color,
                                            radius = this.size.maxDimension - 4f
                                        )
                                    }
                                    .size(18.dp),
                                imageVector = ImageVector.vectorResource(type.icon),
                                contentDescription = type.name,
                                tint = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 14.dp),
                                text = type.name,
                                color = Color.Black
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        category = type
                        typeSelected(type)
                    }
                )
            }
        }
    }
}

@Composable
private fun DropDownCard(
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    listCards: List<CardModel>,
    cardSelected: (CardModel) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var cardItem by remember { mutableStateOf(listCards[0]) }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
                bottom = paddingBottom
            )
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            enabled = false,
            value = "",
            onValueChange = {},
            label = {
                Row {
                    Icon(
                        modifier = Modifier
                            .drawBehind {
                                drawCircle(
                                    color = Wallet,
                                    radius = this.size.maxDimension - 4f
                                )
                            }
                            .size(18.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.icon_wallet),
                        contentDescription = cardItem.nameCard,
                        tint = Color.White
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 14.dp),
                        text = cardItem.nameCard,
                        color = Color.Black
                    )
                }
            },
            trailingIcon = {
                Icon(
                    icon,
                    "Icon Filter",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = Color.Black
                )
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.LightGray
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(textFiledSize.width.dp)
                .offset(y = 0.dp),
        ) {
            listCards.forEach { card ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Icon(
                                modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            color = Wallet,
                                            radius = this.size.maxDimension - 4f
                                        )
                                    }
                                    .size(18.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.icon_wallet),
                                contentDescription = card.nameCard,
                                tint = Color.White
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 14.dp),
                                text = card.nameCard,
                                color = Color.Black
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        cardItem = card
                        cardSelected(card)
                    }
                )
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    onBack: () -> Unit,
    onSaveData: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Añadir deuda") },
        navigationIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaveData
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done"
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
fun AddDebtScreenPreview() {
    MaterialTheme{
        AddDebtScreen(navController = NavController(LocalContext.current))

    }
}
 */