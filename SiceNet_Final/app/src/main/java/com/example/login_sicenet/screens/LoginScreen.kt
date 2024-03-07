package com.example.login_sicenet.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.EnvelopeLogin
import com.example.login_sicenet.network.AddCookiesInterceptor
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavController, viewModel: DataViewModel){
    val context = LocalContext.current
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isValidUser by remember { mutableStateOf(false) }
    var isValidPass by remember { mutableStateOf(false) }
    var passVisible by remember { mutableStateOf(false) }
//    var rememberCredentials by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE0E0E0))
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroundsice),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Card(
                Modifier.padding(12.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    RowImage(navController = navController)
                    RowUser(
                        user = user,
                        userChange = {
                            user = it
                            isValidUser = it.length >= 9 //9
                        },
                        isValidUser = isValidUser
                    )
                    RowPass(
                        pass = pass,
                        passChange = {
                            pass = it
                            isValidPass = pass.length >= 0 //6
                        },
                        passVisible = passVisible,
                        passVisibleChange = {
                            passVisible = !passVisible
                        },
                        isValidPass = isValidPass
                    )
//                    RowRememberCheckbox(
//                        rememberCredentials = rememberCredentials,
//                        onCheckedChange = { isChecked ->
//                            rememberCredentials = isChecked
//                        }
//                    )
                    RowButtonLogin(
                        context = context,
                        isValidUser = isValidUser,
                        isValidPass = isValidPass,
                        nControl = user,
                        password = pass,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RowImage(navController: NavController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navController.navigate("about_screen")
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
                contentDescription = "Imagen de Login",
                modifier = Modifier.size(100.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowPass(
    pass: String,
    passChange: (String) -> Unit,
    passVisible: Boolean,
    passVisibleChange : () -> Unit,
    isValidPass : Boolean
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = pass,
            onValueChange = passChange,
            maxLines = 1,
            singleLine = true,
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { passVisibleChange() }){
                    Icon(imageVector = image, contentDescription = "Ver contraseña")
                }
            },
            visualTransformation = if (passVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (isValidPass) Color.Green else Color.Red,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowUser(
    user: String,
    userChange: (String) -> Unit,
    isValidUser : Boolean
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = user.toUpperCase(),
            onValueChange ={newUser ->
                userChange(newUser.toUpperCase())
            },
            label = { Text("Usuario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            colors = if (isValidUser) {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedBorderColor = Color.Green
                )
            } else {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Red,
                    focusedBorderColor = Color.Red
                )
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowButtonLogin(
    context: Context,
    isValidUser: Boolean,
    isValidPass: Boolean,
    nControl: String,
    password: String,
    navController: NavController,
    viewModel: DataViewModel
){
    val coroutineScope = rememberCoroutineScope()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                if(viewModel.checkInternetConnection(context)){
                    //viewModel.internet=true
                    login(context)
                    viewModel.nControl=nControl
                    viewModel.pass=password
                    viewModel.loginAndGetProfile()
                    viewModel.loginWorkManager(viewModel.nControl,viewModel.pass)
                    viewModel.getCalifFinales()
                    viewModel.getCalifUnidades()
                    viewModel.getKardex()
                    viewModel.getCargaAcademica()

//                    coroutineScope.launch {
//                        viewModel.deleteAccessDB("S20120178")
//                    }
                }else{
                    Log.d("INTERNET","NO HAY INTERNET")
                    //viewModel.internet=false
                    viewModel.nControl=nControl
                    viewModel.pass=password
                    navController.navigate("data_screen")
                }
                    //authenticate(context, nControl, password, navController, viewModel)
            },
            enabled = isValidUser) {
            Text("Iniciar Sesión")
        }
    }
    // Observar el resultado del login
    val loginResult by viewModel.loginResult.observeAsState()

    // Realizar la navegación cuando el login sea exitoso
    if (loginResult == true) {
        navController.navigate("data_screen") // Cambiar al destino deseado después del login exitoso
//        sync(context, "Perfil")
    }
}

//MENSAJES FLOTANTES
fun login(context: Context){
    Toast.makeText(context, "Iniciando sesión", Toast.LENGTH_SHORT).show()
}

fun sync(context: Context,texto: String){
    Toast.makeText(context, "Sincronización de $texto finalizada", Toast.LENGTH_SHORT).show()
}

fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
