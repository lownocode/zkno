package com.quickrise.zkno

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.quickrise.zkno.ui.activities.error.ErrorActivity
import kotlin.system.exitProcess

class App : Application() {
    override fun onCreate() {
        app = this

        super.onCreate()

        createNotificationChannels()

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            handleUncaughtException(e)
        }
    }

    private fun handleUncaughtException(e: Throwable) {
        val intent = Intent(applicationContext, ErrorActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("error", e.stackTraceToString())

        startActivity(intent)
        exitProcess(0)
    }

    private fun createNotificationChannels() {
        val notificationService = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        for (channel in notificationChannels) {
            notificationService.createNotificationChannel(
                NotificationChannel(
                    channel.first,
                    channel.second,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }

    companion object {
        private lateinit var app: App
        val instance: App get() = app
    }
}