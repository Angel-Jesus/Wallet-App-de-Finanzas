package com.angelpr.wallet.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CardModel(
    val id: Int = 0,
    val nameCard: String,
    val creditLineCard: String,
    val typeMoney: String,
    val paidDateExpired: Int,
    val dateClose: Int,
    val colorCard: ULong
)