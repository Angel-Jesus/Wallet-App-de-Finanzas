package com.angelpr.wallet.data.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.angelpr.wallet.R
import com.angelpr.wallet.utils.Constants.MY_CHANNEL_ID

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("receiver", "Se genero exitosamente el broadcast")
        val notificationId = intent?.getIntExtra("notificationId", 0) ?: 0
        val datePaid = intent?.getStringExtra("datePaid") ?: "1"
        notification(context, notificationId, datePaid)
    }

    private fun notification(context: Context, notificationId: Int, datePaid: String) {
        val notification = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_wallet)
            .setContentTitle("Deudas pendientes")
            .setContentText("Su dia de pago es el $datePaid, y aun no ha pagado todas sus deudas, no espere a ultimo momento")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }
}