package com.daviddj.broadcastreceiver_y_telefonia

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

//class IncomingCallReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent?) {
//        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
//            Log.d("BroadcastReceiver", "onReceive started")
//            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
//            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
//                // Recupera el número de teléfono almacenado en SharedPreferences
//                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//                val incomingPhoneNumber = sharedPreferences.getString("incomingPhoneNumber", null)
//                val destinationAddress = sharedPreferences.getString("destinationAddress", null)
//                val textMessage = sharedPreferences.getString("textMessage", "Ahora no puedo contestar")
//                Log.d("BroadcastReceiver", "Incoming phone number: $incomingPhoneNumber")
//                if (incomingPhoneNumber != null &&  incomingPhoneNumber==destinationAddress && textMessage != null) {
//                    Log.d("BroadcastReceiver", "Sending SMS")
//                    var smsManager: SmsManager = SmsManager.getDefault()
//                    smsManager.sendTextMessage(incomingPhoneNumber, null, textMessage, null, null)
//                }else{
//                    Toast.makeText(context,"Error al enviar mensaje", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context,"Ingresa un mensaje y número válidos", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//}

class IncomingCallReceiver : BroadcastReceiver() {
    private var messageSent = false
    private var lastState = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            Log.d("BroadcastReceiver", "onReceive started")
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING && lastState != TelephonyManager.EXTRA_STATE_RINGING && !messageSent) {
                // Recupera el número de teléfono almacenado en SharedPreferences
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val incomingPhoneNumber = sharedPreferences.getString("incomingPhoneNumber", null)
                val destinationAddress = sharedPreferences.getString("destinationAddress", null)
                val textMessage = sharedPreferences.getString("textMessage", "Ahora no puedo contestar")
                Log.d("BroadcastReceiver", "Incoming phone number: $incomingPhoneNumber")
                if (incomingPhoneNumber != null &&  incomingPhoneNumber==destinationAddress && textMessage != null) {
                    Log.d("BroadcastReceiver", "Sending SMS")
                    var smsManager: SmsManager = SmsManager.getDefault()
                    try {
                        val sentPI = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"),
                            PendingIntent.FLAG_IMMUTABLE)
                        context.registerReceiver(object : BroadcastReceiver() {
                            override fun onReceive(context: Context, intent: Intent) {
                                when (resultCode) {
                                    Activity.RESULT_OK -> Toast.makeText(context, "SMS enviado", Toast.LENGTH_SHORT).show()
                                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(context, "Error genérico", Toast.LENGTH_SHORT).show()
                                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(context, "Sin servicio", Toast.LENGTH_SHORT).show()
                                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(context, "PDU nulo", Toast.LENGTH_SHORT).show()
                                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(context, "Radio apagada", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }, IntentFilter("SMS_SENT"), Context.RECEIVER_NOT_EXPORTED)
                        smsManager.sendTextMessage(incomingPhoneNumber, null, textMessage, sentPI, null)
                        messageSent = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context,"Error al enviar mensaje", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context,"Ingresa un mensaje y número válidos", Toast.LENGTH_LONG).show()
                }
            } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                messageSent = false
            }
            lastState = state?:""
        }
    }
}


