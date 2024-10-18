package com.angelpr.wallet.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.angelpr.wallet.data.db.dao.WalletDao
import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity

@Database(entities = [CardWalletEntity::class, DebtsWalletEntity::class], version = 5)
abstract class WalletDatabase: RoomDatabase() {
    abstract fun getCardDao(): WalletDao
}