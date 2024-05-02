package com.daviddj.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("Nuevo Token FCM", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel(this)

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d("MENSAJE EN PRIMER PLANO", "Data Payload: " + remoteMessage.data)

            // Aquí puedes personalizar la notificación según los datos recibidos
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background)

            // Obtén el NotificationManager
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Muestra la notificación
            notificationManager.notify(0, notificationBuilder.build())
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MENSAJE EN PRIMER PLANO", "Message Notification Body: ${it.body}")
        }
    }
}

// Crea el canal de notificación
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Nombre_del_Canal"
        val descriptionText = "Descripción_del_Canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        // Registra el canal en el sistema
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}
