package com.daviddj.broadcastreceiver_y_telefonia

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsScreen() {
    //PERMISOS
    val calLogPermission = rememberPermissionState(
        Manifest.permission.READ_CALL_LOG
    )
    val smsPermission = rememberPermissionState(
        Manifest.permission.SEND_SMS
    )
    val phonePermission = rememberPermissionState(
        Manifest.permission.READ_PHONE_STATE
    )
    //Realiza un seguimiento del estado del diálogo de justificación, necesario cuando el usuario requiere más justificación
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    Row{
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Show rationale dialog when needed
            rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }
            PermissionRequestButton(
                isGranted = smsPermission.status.isGranted,
                title = "Permiso de envio de SMS",
                smsPermission = smsPermission,
                phonePermission = phonePermission,
                calLogPermission = calLogPermission

            ){
                if (smsPermission.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Permiso para enviar SMS",
                        "In order to use this feature please grant access by accepting " + "the enviar SMS." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            smsPermission.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    smsPermission.launchPermissionRequest()
                }
            }
            PermissionRequestButton(
                isGranted = phonePermission.status.isGranted,
                title = "Permiso de acceso al teléfono",
                smsPermission = smsPermission,
                phonePermission = phonePermission,
                calLogPermission = calLogPermission
            ){
                if (phonePermission.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Permiso para acceder al estado del teléfono",
                        "In order to use this feature please grant access by accepting " + "the estado del teléfono." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            phonePermission.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    phonePermission.launchPermissionRequest()
                }
            }
            PermissionRequestButton(
                isGranted = calLogPermission.status.isGranted,
                title = "Permiso de Cal Log",
                smsPermission = smsPermission,
                phonePermission = phonePermission,
                calLogPermission = calLogPermission
            ){
                if (calLogPermission.status.shouldShowRationale) {
                    rationaleState = RationaleState(
                        "Permiso para acceder al estado del teléfono",
                        "In order to use this feature please grant access by accepting " + "the Cal Log." + "\n\nWould you like to continue?",
                    ) { proceed ->
                        if (proceed) {
                            calLogPermission.launchPermissionRequest()
                        }
                        rationaleState = null
                    }
                } else {
                    calLogPermission.launchPermissionRequest()
                }
            }
        }
    }
}

@Composable
fun PermissionRationaleDialog(rationaleState: RationaleState) {
    AlertDialog(onDismissRequest = { rationaleState.onRationaleReply(false) }, title = {
        Text(text = rationaleState.title)
    }, text = {
        Text(text = rationaleState.rationale)
    }, confirmButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(true)
        }) {
            Text("Continue")
        }
    }, dismissButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(false)
        }) {
            Text("Dismiss")
        }
    })
}
data class RationaleState(
    val title: String,
    val rationale: String,
    val onRationaleReply: (proceed: Boolean) -> Unit,
)

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PermissionRequestButton(
    isGranted: Boolean, title: String,
    smsPermission: PermissionState,
    phonePermission: PermissionState,
    calLogPermission: PermissionState,
    onClick: () -> Unit,
) {
    var responderNumber by remember { mutableStateOf("") }
    var smsText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    if (smsPermission.status.isGranted && phonePermission.status.isGranted && calLogPermission.status.isGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = responderNumber,
                onValueChange = { responderNumber = it },
                label = { Text("Responder Number") },
                leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = smsText,
                onValueChange = { smsText = it },
                label = { Text("SMS Text") },
                leadingIcon = { Icon(imageVector = Icons.Default.MailOutline, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LaunchedEffect(responderNumber, smsText) {
                with(sharedPref.edit()) {
                    putString("destinationAddress", responderNumber)
                    putString("textMessage", smsText)
                    apply()
                }
            }
        }
    } else {
        Button(onClick = onClick) {
            Text(if (isGranted) "$title concedido" else "Request $title")        }
    }
}