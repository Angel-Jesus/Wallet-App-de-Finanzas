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
    @ColumnInfo(name = "nameCard")
    val nameCard: String,
    @ColumnInfo(name = "typeMoney")
    val typeMoney: String,
    @ColumnInfo(name = "debt")
    val debt: Float,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "quotePaid")
    val quotePaid: Int = 0,
    @ColumnInfo(name = "quotas")
    val quotas: Int,
    @ColumnInfo(name = "isPaid")
    val isPaid: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "dateExpired")
    val dateExpired: Long,

)

fun DebtsWalletEntity.toDebtsWallet(): DebtModel =
    DebtModel(
        id = id,
        idWallet = idWallet,
        nameCard = nameCard,
        typeMoney = typeMoney,
        debt = debt,
        type = type,
        quotePaid = quotePaid,
        quotas = quotas,
        isPaid = isPaid,
        date = date,
        dateExpired = dateExpired
    )

fun DebtModel.toDebtsWalletEntity(): DebtsWalletEntity =
    DebtsWalletEntity(
        id = id,
        idWallet = idWallet,
        nameCard = nameCard,
        typeMoney = typeMoney,
        debt = debt,
        type = type,
        quotePaid = quotePaid,
        quotas = quotas,
        isPaid = isPaid,
        date = date,
        dateExpired = dateExpired
    )

