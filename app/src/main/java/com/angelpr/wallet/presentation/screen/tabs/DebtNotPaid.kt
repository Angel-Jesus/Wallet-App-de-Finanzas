package com.angelpr.wallet.presentation.screen.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelpr.wallet.R
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.EmptyStateScreen
import com.angelpr.wallet.presentation.components.PieChart
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.presentation.components.model.getCategory
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.Wallet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DebtNotPaid(
    emptyStateNotPaid: Boolean,
    debtTypeList: Map<String, Type>,
    uiDebtState: WalletViewModel.UiStateDebt,
    showPaidQuoteDialog: MutableState<Boolean>,
    indexDebt: MutableIntState
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(
                align = if (emptyStateNotPaid) {
                    Alignment.Center
                } else {
                    Alignment.TopStart
                }
            )
    ) {
        if (emptyStateNotPaid) {
            item {
                EmptyStateScreen(
                    title = "Nada registrado",
                    text = "No tiene deuda pendiente o registrada"
                )
            }
        } else {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PieChart(
                        data = debtTypeList,
                        chartBarWidth = 30.dp
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    text = "Deudas pendientes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            itemsIndexed(uiDebtState.debtNotPaidList) { index, debtModel ->
                CardDebtItem(
                    onClick = {
                        showPaidQuoteDialog.value = true
                        indexDebt.intValue = index
                    },
                    debtModel = debtModel
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun CardDebtItem(
    onClick: () -> Unit,
    debtModel: DebtModel
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.ofEpochDay(debtModel.date)
    val dateExpired = LocalDate.ofEpochDay(debtModel.dateExpired)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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

