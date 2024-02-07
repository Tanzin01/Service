package com.imam.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import android.net.VpnService


class MyBackgroundService : VpnService() {

    private val channelId = "notification_channel"
    private val notificationId = 101
    
    private lateinit var vpnThread: Thread
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showForegroundNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Legit VPN")
            .setContentText("Connected TO Singapore...")
            .setSmallIcon(R.drawable.baseline_circle_notifications_24) // Ensure you have this drawable
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(notificationId, notification)
    }
    
    private fun startVpn() {
        vpnThread = Thread {
            try {
                if (vpnInterface == null) {
                    // Start VPN connection setup only if it's not already started
                    val builder = Builder()
                        .setSession(getString(R.string.app_name))
                        .addAddress("10.0.0.1", 24)
                        .addRoute("0.0.0.0", 0)
                    vpnInterface = builder.establish()
                    if (vpnInterface == null) {
                        Log.e("MyVpnService", "Null, starting VPN")
                    }
                }
            } catch (e: Exception) {
                Log.e("MyVpnService", "Error starting VPN", e)
                showErrorToast("Error starting VPN: ${e.message}")
            }
        }
        vpnThread.start()
    }
    
    private fun showErrorToast(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
}