package com.daviddj.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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

    private val viewModel: MessageViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(MessageViewModel::class.java)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("ENVIADO DESDE:", "From: ${remoteMessage.from}")

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("CUERPO DEL MENSAJE", "Message Notification Body: ${it.body}")
//            val messageBody = it.body ?: ""
//            val messageTitle = it.title ?: ""
//            viewModel.setMessage(messageBody)
//            viewModel.setTitle(messageTitle)
        }

//        Looper.prepare()
//
//        Handler().post{
//            Toast.makeText(baseContext,remoteMessage.notification!!.body,Toast.LENGTH_LONG).show()
//        }
//
//        Looper.loop()



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}