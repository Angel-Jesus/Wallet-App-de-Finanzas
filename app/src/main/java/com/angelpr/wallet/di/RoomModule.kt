package com.angelpr.wallet.di

import android.content.Context
import androidx.room.Room
import com.angelpr.wallet.data.db.WalletDatabase
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
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WalletDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideCardDai(db: WalletDatabase) = db.getCardDao()
}