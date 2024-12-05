package com.angelpr.wallet.di

import android.content.Context
import androidx.room.Room
import com.angelpr.wallet.data.db.WalletDatabase
import com.angelpr.wallet.data.repository.WalletRepository
import com.angelpr.wallet.domain.wallet.AddWalletUseCase
import com.angelpr.wallet.domain.wallet.DeleteWalletUseCase
import com.angelpr.wallet.domain.wallet.GetWalletUseCase
import com.angelpr.wallet.domain.wallet.UpdateWalletUseCase
import com.angelpr.wallet.domain.WalletUseCases
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
        Room.databaseBuilder(context, WalletDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideWalletRepository(db: WalletDatabase): WalletRepository =
        WalletRepository(db.getCardDao())

    @Singleton
    @Provides
    fun provideWalletUseCases(repository: WalletRepository): WalletUseCases =
        WalletUseCases(
            addWallet = AddWalletUseCase(repository),
            getWallet = GetWalletUseCase(repository),
            updateWallet = UpdateWalletUseCase(repository),
            deleteWallet = DeleteWalletUseCase(repository)
        )
}