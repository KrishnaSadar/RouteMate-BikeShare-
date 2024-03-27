package com.example.driverapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.driverapp.ongoingTrip.RequestActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var mNotificationManager: NotificationManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Play notification sound
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r: Ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }

        // Vibrate device
        val v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 300, 300, 300)
        v.vibrate(pattern, -1)
        val resultIntent = Intent(this, RequestActivity::class.java).apply {
            putExtra("body", remoteMessage.notification?.body.toString())
            putExtra("finishActivity", true)
        }

        val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE)
        // Create notification
        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        builder.setSmallIcon(R.drawable.logo1)
            .setContentTitle(remoteMessage.notification?.title+" wants to share ride")
            .setContentText(remoteMessage.notification?.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body))
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)


        mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create notification channel if API level is higher than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }



        // Notify
        mNotificationManager.notify(100, builder.build())
    }
}
