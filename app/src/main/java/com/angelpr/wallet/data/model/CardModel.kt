package com.angelpr.wallet.data.model

data class CardModel(
    val id: Int = 0,
    val nameCard: String,
    val creditLineCard: String,
    val typeMoney: String,
    val paidDateExpired: Int,
    val dateClose: Int,
    val colorCard: ULong
)