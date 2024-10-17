package com.angelpr.wallet.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.angelpr.wallet.data.model.CardModel

@Entity(tableName = "cardWallet_table")
data class CardWalletEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "nameCard")
    val nameCard: String,
    @ColumnInfo(name = "creditLineCard")
    val creditLineCard: String,
    @ColumnInfo(name = "typeMoney")
    val typeMoney: String,
    @ColumnInfo(name = "paidDateExpired")
    val paidDateExpired: Int,
    @ColumnInfo(name = "dateClose")
    val dateClose: Int,
    @ColumnInfo(name = "colorCard")
    val colorCard: String
)

fun CardWalletEntity.toCardWallet(): CardModel =
    CardModel(
        id = id,
        nameCard = nameCard,
        creditLineCard = creditLineCard,
        typeMoney = typeMoney,
        paidDateExpired = paidDateExpired,
        dateClose = dateClose,
        colorCard = colorCard.toULong()
    )

fun CardModel.toCardWalletEntity(): CardWalletEntity = CardWalletEntity(
    id = id,
    nameCard = nameCard,
    creditLineCard = creditLineCard,
    typeMoney = typeMoney,
    paidDateExpired = paidDateExpired,
    dateClose = dateClose,
    colorCard = colorCard.toString()
)
