package com.daviddj.broadcastreceiver_y_telefonia

import android.content.Context
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log

class MyCallScreeningService : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle.schemeSpecificPart
        Log.d("CallScreeningService", "Incoming phone number: $phoneNumber")
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("incomingPhoneNumber", phoneNumber).apply()
    }
}