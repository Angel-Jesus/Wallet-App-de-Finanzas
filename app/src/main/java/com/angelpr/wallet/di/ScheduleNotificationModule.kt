package com.angelpr.wallet.di

import android.app.AlarmManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleNotificationModule {

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

}