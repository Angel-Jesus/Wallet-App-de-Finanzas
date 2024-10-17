package com.angelpr.wallet.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.angelpr.wallet.data.model.DebtModel

@Entity(tableName = "debtsWallet_table")
data class DebtsWalletEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "idWallet")
    val idWallet: Int,
    @ColumnInfo(name = "debt")
    val debt: Int,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "isPaid")
    val isPaid: Int,
    @ColumnInfo(name = "date")
    val date: Long
)

fun DebtsWalletEntity.toDebtsWallet(): DebtModel =
    DebtModel(id = id, idWallet = idWallet, debt = debt, type = type, isPaid = isPaid, date = date)

fun DebtModel.toDebtsWalletEntity(): DebtsWalletEntity =
    DebtsWalletEntity(id = id, idWallet = idWallet, debt = debt, type = type, isPaid = isPaid, date = date)

