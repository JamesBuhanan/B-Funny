package com.example.b_funny.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.b_funny.BuildConfig
import com.example.b_funny.R

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

fun sendNotification(context: Context, message: String, title: String) {
    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val name = context.getString(R.string.app_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(getUniqueId(), notification)
}

private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())