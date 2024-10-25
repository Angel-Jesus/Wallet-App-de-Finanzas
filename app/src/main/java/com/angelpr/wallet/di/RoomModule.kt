package com.angelpr.wallet.di

import android.content.Context
import androidx.room.Room
import com.angelpr.wallet.data.db.WalletDatabase
import com.angelpr.wallet.data.db.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "card_db"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): WalletDatabase =
        Room.databaseBuilder(context, WalletDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideCardDai(db: WalletDatabase): WalletDao = db.getCardDao()
}