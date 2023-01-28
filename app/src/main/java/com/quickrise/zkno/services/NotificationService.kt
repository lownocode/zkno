package com.quickrise.zkno.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.quickrise.zkno.R
import com.quickrise.zkno.ui.activities.splash_screen.SplashScreenActivity

class NotificationService : FirebaseMessagingService() {
    private data class Data(
        val title: String,
        val body: String,
        val channelId: String,
    )

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        getSharedPreferences("USER", MODE_PRIVATE).edit().putString("notifyToken", token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val data = Data(
            message.data["title"] ?: "Заголовок уведомления",
            message.data["body"] ?: "Тело уведомления",
            message.data["channelId"] ?: "main",
        )

        manager.notify(System.nanoTime().toInt(), getNotification(data))
    }

    private fun getNotification(data: Data): Notification {
        return NotificationCompat
            .Builder(this, data.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(data.title)
            .setAutoCancel(true)
            .setContentText(data.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(data.body))
            .setChannelId(data.channelId)
            .setContentIntent(getPendingIntent(getDefaultIntent()))
            .build()
    }

    private fun getDefaultIntent(): Intent {
        val intent = Intent(this, SplashScreenActivity::class.java)

        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return intent
    }

    private fun getPendingIntent(defaultIntent: Intent): PendingIntent {
        return PendingIntent.getActivities(
            this,
            0,
            arrayOf(defaultIntent),
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}