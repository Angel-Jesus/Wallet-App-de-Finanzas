package com.angelpr.wallet.presentation.screen.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelpr.wallet.R
import com.angelpr.wallet.data.model.DebtModel
import com.angelpr.wallet.presentation.components.EmptyStateScreen
import com.angelpr.wallet.presentation.components.model.getCategory
import com.angelpr.wallet.presentation.viewmodel.WalletViewModel
import com.angelpr.wallet.ui.theme.Paid
import com.angelpr.wallet.ui.theme.Wallet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DebtPaid(
    uiDebtState: WalletViewModel.UiStateDebt,
    oncClick: (DebtModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(
                align = if (uiDebtState.debtPaidList.isEmpty()) {
                    Alignment.Center
                } else {
                    Alignment.TopStart
                }
            )
    ) {
        if (uiDebtState.debtPaidList.isEmpty()) {
            item {
                EmptyStateScreen(
                    title = "Nada registrado",
                    text = "No tiene deudas canceladas"
                )
            }
        } else {
            item {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 12.dp) ,
                    text = "Historial",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(uiDebtState.debtPaidList){ debtModel ->
                CardDebtPaidtItem(
                    debtModel = debtModel,
                    onClick = { oncClick(debtModel) }
                )
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun CardDebtPaidtItem(
    debtModel: DebtModel,
    onClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val date = LocalDate.ofEpochDay(debtModel.date)

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
                    color = Paid
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
                    text = "Cancelado",
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
