package com.angelpr.wallet.data.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.angelpr.wallet.MainActivity
import com.angelpr.wallet.R
import com.angelpr.wallet.utils.Constants.MY_CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("receiver", "Se genero exitosamente el broadcast")
        val cardname = intent?.getStringExtra("cardName") ?: "Tarjeta Null"
        val notificationId = intent?.getIntExtra("notificationId", 0) ?: 0
        val datePaid = intent?.getStringExtra("datePaid") ?: "1"
        notification(context, cardname, notificationId, datePaid)
    }

    private fun notification(
        context: Context,
        cardName: String,
        notificationId: Int,
        datePaid: String
    ) {
        // Create Intent to launch app when notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        // Create notification
        val notification = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_wallet)
            .setContentTitle("Deudas pendientes en la tarjeta $cardName")
            .setContentText("Faltan pocos dias para tu fecha de pago")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Su dia de pago es el $datePaid, y aun no ha pagado todas sus deudas, no espere a ultimo momento")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }
}