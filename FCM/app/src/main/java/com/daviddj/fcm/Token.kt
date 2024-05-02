package com.daviddj.fcm

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Screen(){
    val context = LocalContext.current
    notificacion(context)
    TopAppBar(title = { Text(text = "Notificaciones FireBase") },
        navigationIcon = {
         IconButton(onClick = { Toast.makeText(context,"App de Notificaciones Firebase",Toast.LENGTH_SHORT).show()}) {
             androidx.compose.material3.Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "icono")
         }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Yellow,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        ))
}

private fun notificacion(context: Context){
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        // Log and toast
        val msg = context.getString(R.string.msg_token_fmt, token)
        Log.d(TAG, msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    })
}