package com.angelpr.wallet.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.angelpr.wallet.data.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) {

    fun schedule(notificationId: Int, year: Int, month: Int, day: Int){

        val datePaid = notificationId.toString()
        val yearPaid = datePaid.substring(0, 4)
        val monthPaid = datePaid.substring(4, 6)
        val dayPaid = datePaid.substring(6, 8)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("notificationId", notificationId)
            putExtra("datePaid", "$dayPaid/$monthPaid/$yearPaid")
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
        }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + 8000,
            pendingIntent
        )
    }

    fun cancel(notificationId: Int){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                notificationId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.d("receiver", "cancelado id: $notificationId")
    }

}