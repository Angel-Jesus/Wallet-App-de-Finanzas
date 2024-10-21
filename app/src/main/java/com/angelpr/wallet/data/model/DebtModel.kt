package com.angelpr.wallet.data.model

data class DebtModel(
    val id: Int = 0,
    val idWallet: Int,
    val nameCard: String,
    val typeMoney: String,
    val debt: Float,
    val type: String,
    val quotePaid: Int = 0,
    val quotas: Int,
    val isPaid: Int,
    val date: Long,
    val dateExpired: Long,
)