package com.angelpr.wallet.presentation.screen

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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.angelpr.wallet.data.model.CardModel
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.CardWalletList
import com.angelpr.wallet.ui.theme.GreenTopBar


@Composable
fun AddWalletScreen(
    viewModel: WalletViewModel,
    navController: NavController
) {
    var nameWallet by remember { mutableStateOf("") }
    var creditLine by remember { mutableStateOf("0") }
    var typeMoney by remember { mutableStateOf("PEN") }
    var dayExpiration by remember { mutableStateOf("0") }
    var dateClose by remember { mutableStateOf("0") }
    var colorCard by remember { mutableStateOf(CardWalletList[0]) }

    Scaffold(
        topBar = {
            TopBar(navController) {
                viewModel.addNewCard(
                    CardModel(
                        nameCard = nameWallet,
                        creditLineCard = creditLine,
                        typeMoney = typeMoney,
                        paidDateExpired = dayExpiration.toInt(),
                        dateClose = dateClose.toInt(),
                        colorCard = colorCard.value
                    )
                )
                navController.popBackStack()
            }
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
                value = nameWallet,
                onValueChange = { newValue ->
                    nameWallet = newValue
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
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
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
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
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray
                )
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
                    if (newValue == ""){
                        dayExpiration = newValue
                    } else if(newValue.toInt() in 0..31){
                        dayExpiration = newValue
                    }
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
                    if(newValue == ""){
                        dateClose = newValue
                    } else if (newValue.toInt() in 0..31){
                        dateClose = newValue
                    }
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
                text = "Color",
                color = Color.Gray,
                fontSize = 13.sp
            )
            DropDownColors(
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
    paddingStart: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    colorSelected: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var colorCard by remember { mutableStateOf(CardWalletList[0]) }
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
                        colorSelected(color)
                    }
                )
            }
        }
    }


}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    navController: NavController,
    saveData: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Añadir cuenta") },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            IconButton(
                onClick = saveData
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
fun AddWalletPreview(){
    MaterialTheme {
        AddWalletScreen(
            navController = rememberNavController()
        )
    }
}

 */
