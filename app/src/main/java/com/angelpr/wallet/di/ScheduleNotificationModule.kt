package com.angelpr.wallet.di

import android.app.AlarmManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.angelpr.wallet.data.repository.DataStoreRepository
import com.angelpr.wallet.data.repository.NotificationRepository
import com.angelpr.wallet.domain.DataStoreUseCase
import com.angelpr.wallet.domain.NotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleNotificationModule {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    fun provideNotificationRepository(
        @ApplicationContext context: Context,
        alarmManager: AlarmManager
    ): NotificationRepository =
        NotificationRepository(alarmManager = alarmManager, context = context)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository =
        DataStoreRepository(dataStore)

    @Singleton
    @Provides
    fun provideNotificationUseCase(repository: NotificationRepository): NotificationUseCase =
        NotificationUseCase(repository)

    @Singleton
    @Provides
    fun provideDataStoreUseCase(repository: DataStoreRepository): DataStoreUseCase =
        DataStoreUseCase(repository)

}
/*
val notificationId = date.year*10000 + date.monthValue*100 + date.dayOfMonth
viewModel.cancelScheduleNotification(notificationId)
 */