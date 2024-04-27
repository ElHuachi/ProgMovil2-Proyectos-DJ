package com.daviddj.fcm

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Icon
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Screen(viewModel: MessageViewModel){
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

    val messageBody = viewModel.getMessage()
    val messageTitle = viewModel.getTitle()
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