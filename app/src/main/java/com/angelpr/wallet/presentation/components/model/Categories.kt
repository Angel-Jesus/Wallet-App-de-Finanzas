package com.angelpr.wallet.presentation.components.model

import androidx.compose.ui.graphics.Color
import com.angelpr.wallet.R
import com.angelpr.wallet.ui.theme.Entertaiment
import com.angelpr.wallet.ui.theme.Food
import com.angelpr.wallet.ui.theme.Service
import com.angelpr.wallet.ui.theme.Shopping
import com.angelpr.wallet.ui.theme.Transportation

object Categories {

    val Debt = listOf(
        Type(
            name = "Comida y Bebida",
            icon = R.drawable.category_food,
            color = Food,
        ),
        Type(
            name = "Compras",
            icon = R.drawable.category_shoppin,
            color = Shopping
        ),
        Type(
            name = "Transporte",
            icon = R.drawable.category_transportation,
            color = Transportation
        ),
        Type(
            name = "Vida y Entretenimiento",
            icon = R.drawable.category_entertainment,
            color = Entertaiment
        ),
        Type(
            name = "Servicios",
            icon = R.drawable.category_service,
            color = Service
        )
    )
}

fun getCategory(category: String): Type = when (category) {
    Categories.Debt[0].name -> Categories.Debt[0]
    Categories.Debt[1].name -> Categories.Debt[1]
    Categories.Debt[2].name -> Categories.Debt[2]
    Categories.Debt[3].name -> Categories.Debt[3]
    else -> Categories.Debt[4]
}

data class Type(
    val name: String,
    val icon: Int,
    val color: Color,
    val typeMoney: String = "PEN",
    var value: Float = 0.0f
)