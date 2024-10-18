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

data class Type(
    val name: String,
    val icon: Int,
    val color: Color,
    val typeMoney: String = "PEN",
    var value: Float = 0.0f
)