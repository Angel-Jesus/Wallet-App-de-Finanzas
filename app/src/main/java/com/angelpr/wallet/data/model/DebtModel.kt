package com.angelpr.wallet.data.model

import androidx.room.ColumnInfo

data class DebtModel(
    val id: Int = 0,
    val idWallet: Int,
    val debt: Float,
    val type: String,
    val isPaid: Int,
    val date: Long
)