package com.angelpr.wallet.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.presentation.components.MessageDialog
import com.angelpr.wallet.presentation.screen.event.CardsEvent
import com.angelpr.wallet.presentation.screen.event.DebtsEvent
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.CardWalletList
import com.angelpr.wallet.ui.theme.GreenTopBar
import com.angelpr.wallet.utils.getDateExpired
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun AddEditWalletScreen(
    viewModel: WalletViewModel,
    modeEdit: Boolean,
    navController: NavController
) {
    val uiStateCard by viewModel.stateCard.collectAsState()
    val uiStateDebt by viewModel.stateDebt.collectAsState()

    val cardModel = if (modeEdit) uiStateCard.cardSelected else null

    LaunchedEffect(key1 = true) {
        viewModel.eventFlowAddEditCard.collectLatest { event ->
            when (event) {
                WalletViewModel.UiEvent.Success -> navController.popBackStack()
            }
        }
    }

    var nameCard by remember { mutableStateOf(cardModel?.nameCard ?: "") }
    var creditLine by remember { mutableStateOf(cardModel?.creditLineCard ?: "0") }
    var typeMoney by remember { mutableStateOf(cardModel?.typeMoney ?: "PEN") }
    var dayExpiration by remember { mutableStateOf((cardModel?.paidDateExpired ?: 0).toString()) }
    var dateClose by remember { mutableStateOf((cardModel?.dateClose ?: 0).toString()) }
    var colorCard by remember { mutableStateOf(cardModel?.colorCard ?: CardWalletList[0].value) }


    var showDeleteDialog by remember { mutableStateOf(false) }


    if (showDeleteDialog) {
        MessageDialog(
            onDismissRequest = { showDeleteDialog = false },
            positiveButton = {
                if (uiStateCard.cardSelected != null) {

                    val datesExpiredList = uiStateDebt.debtNotPaidList.map { it.dateExpired }.distinct()

                    val cardDelete = CardModel(
                        id = uiStateCard.cardSelected!!.id,
                        nameCard = nameCard,
                        creditLineCard = creditLine,
                        typeMoney = typeMoney,
                        paidDateExpired = dayExpiration.toInt(),
                        dateClose = dateClose.toInt(),
                        colorCard = colorCard
                    )
                    showDeleteDialog = false
                    viewModel.onEventDebt(DebtsEvent.DeleteAllDebts(cardDelete.id))
                    viewModel.onEventCard(CardsEvent.DeleteCard(cardDelete))
                    viewModel.cancelScheduleNotification(cardDelete.nameCard, datesExpiredList)

                }
            },
            negativeButton = { showDeleteDialog = false },
            title = "Eliminar tarjeta",
            text = "Si elimina la tarjeta se eliminarán todos los registros de deuda vinculadas a ella. ¿Desea continuar?"
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                modeEdit = modeEdit,
                onBack = { navController.popBackStack() },
                onDeleteData = { showDeleteDialog = true },
                onSaveData = {
                    if (modeEdit) {
                        viewModel.onEventCard(
                            CardsEvent.UpdateCard(
                                CardModel(
                                    id = cardModel!!.id,
                                    nameCard = nameCard,
                                    creditLineCard = creditLine,
                                    typeMoney = typeMoney,
                                    paidDateExpired = dayExpiration.toInt(),
                                    dateClose = dateClose.toInt(),
                                    colorCard = colorCard
                                )
                            )
                        )
                    } else {
                        viewModel.onEventCard(
                            CardsEvent.AddCard(
                                CardModel(
                                    nameCard = nameCard,
                                    creditLineCard = creditLine,
                                    typeMoney = typeMoney,
                                    paidDateExpired = dayExpiration.toInt(),
                                    dateClose = dateClose.toInt(),
                                    colorCard = colorCard
                                )
                            )
                        )
                    }
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
                text = "Nombre de la cuenta",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                value = nameCard,
                onValueChange = { newValue ->
                    nameCard = newValue
                },
                singleLine = true,
                /*
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
                 */
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Linea de credito",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { isFocused ->
                        if (isFocused.isFocused) {
                            if (creditLine == "0") {
                                creditLine = ""
                            }
                        } else {
                            if (creditLine == "") {
                                creditLine = "0"
                            }
                        }
                    },
                value = creditLine,
                onValueChange = { newValue ->
                    creditLine = newValue
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                /*
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
                 */
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Moneda",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                value = typeMoney,
                onValueChange = { newValue ->
                    typeMoney = newValue
                },
                /*
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )

                 */
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Fecha de vencimiento del pago",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { isFocused ->
                        if (isFocused.isFocused) {
                            if (dayExpiration == "0") {
                                dayExpiration = ""
                            }
                        } else {
                            if (dayExpiration == "") {
                                dayExpiration = "0"
                            }
                        }
                    },
                value = dayExpiration,
                onValueChange = { newValue ->
                    if (newValue == "") {
                        dayExpiration = newValue
                    } else if (newValue.toInt() in 0..31) {
                        dayExpiration = newValue
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                /*
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )

                 */
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Fecha de cierre",
                color = Color.Gray,
                fontSize = 13.sp
            )

            TextField(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .onFocusChanged { isFocused ->
                        if (isFocused.isFocused) {
                            if (dateClose == "0") {
                                dateClose = ""
                            }
                        } else {
                            if (dateClose == "") {
                                dateClose = "0"
                            }
                        }
                    },
                value = dateClose,
                onValueChange = { newValue ->
                    if (newValue == "") {
                        dateClose = newValue
                    } else if (newValue.toInt() in 0..31) {
                        dateClose = newValue
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                /*
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )

                 */
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp),
                text = "Color",
                color = Color.Gray,
                fontSize = 13.sp
            )
            DropDownColors(
                color = colorCard,
                paddingStart = 8.dp,
                paddingEnd = 8.dp,
                paddingTop = 8.dp
            ) { color ->
                colorCard = color
            }
        }
    }
}

@Composable
private fun DropDownColors(
    color: ULong,
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    colorSelected: (ULong) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var colorCard by remember { mutableStateOf(Color(color)) }
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
                Card(
                    modifier = Modifier
                        .fillMaxHeight(0.08f)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = colorCard
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) { }
            },
            trailingIcon = {
                Icon(
                    icon,
                    "Icon Filter",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = Color.Black
                )
            },
            /*
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
                disabledIndicatorColor = Color.LightGray
            )

             */
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(textFiledSize.width.dp)
                .offset(y = 0.dp),
        ) {
            CardWalletList.forEach { color ->
                DropdownMenuItem(
                    text = {
                        Card(
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = color
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) { }
                    },
                    onClick = {
                        colorCard = color
                        expanded = false
                        colorSelected(color.value)
                    }
                )
            }
        }
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    modeEdit: Boolean,
    onBack: () -> Unit,
    onDeleteData: () -> Unit,
    onSaveData: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Añadir cuenta") },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            if (modeEdit) {
                IconButton(
                    onClick = onDeleteData
                ) {
                    Icon(
                        tint = Color.White,
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }

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

