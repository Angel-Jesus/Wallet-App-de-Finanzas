package com.angelpr.wallet.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.angelpr.wallet.data.receiver.AlarmReceiver
import com.angelpr.wallet.domain.repository.NotificacionRepository
import java.time.LocalDate
import java.util.Calendar

class NotificationRepositoryImpl(
    private val alarmManager: AlarmManager,
    private val context: Context
): NotificacionRepository {

    override fun schedule(
        cardName: String,
        dateExpired: LocalDate,
        notificationId: Int,
        year: Int,
        month: Int,
        day: Int
    ) {
        val dateExpiredDebt = "${dateExpired.dayOfMonth}/${ dateExpired.monthValue }/${dateExpired.year}"

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("cardName", cardName)
            putExtra("notificationId", notificationId)
            putExtra("datePaid", dateExpiredDebt)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
        }.timeInMillis

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + 15000, //Calendar.getInstance().timeInMillis + 15000
            pendingIntent
        )
    }

    override fun cancel(notificationId: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                notificationId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}