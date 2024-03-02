package com.example.login_sicenet.screens

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.EnvelopeLogin
import com.example.login_sicenet.network.AddCookiesInterceptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun LoginScreen(navController: NavController, viewModel: DataViewModel){
    val context = LocalContext.current
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isValidUser by remember { mutableStateOf(false) }
    var isValidPass by remember { mutableStateOf(false) }
    var passVisible by remember { mutableStateOf(false) }
    var rememberCredentials by rememberSaveable { mutableStateOf(false) }

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
                    RowRememberCheckbox(
                        rememberCredentials = rememberCredentials,
                        onCheckedChange = { isChecked ->
                            rememberCredentials = isChecked
                        }
                    )
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
//                unfocusedIndicatorColor = if (isValidPass) Color.Green else Color.Red
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowRememberCheckbox(
    rememberCredentials: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = rememberCredentials,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text("Recordar credenciales",
            modifier = Modifier
                .padding(vertical = 13.dp))
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
        OutlinedTextField(value = user, onValueChange = userChange,
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
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                    login(context)
                    authenticate(context, nControl, password, navController, viewModel)
            },
            enabled = isValidUser) {
            Text("Iniciar Sesión")
        }
    }
}

//CREACION DE REQEUSTS AL SERVIDOR
//CREACION DE REQEUSTS AL SERVIDOR
private fun authenticate(context: Context, matricula: String, contrasenia: String, navController: NavController , viewModel: DataViewModel) {
    val bodyLogin = loginRequestBody(matricula, contrasenia)
    val service = RetrofitClient(context).retrofitService
    service.login(bodyLogin).enqueue(object : Callback<EnvelopeLogin>{
        override fun onResponse(call: Call<EnvelopeLogin>, response: Response<EnvelopeLogin>){
            if (response.isSuccessful) {
                val envelope = response.body()
                val accesoResultJson: String? = envelope?.bodyLogin?.accesoLoginResponse?.accesoLoginResult

                // Deserializa la cadena JSON a AlumnoAcademicoResult
                val json = Json { ignoreUnknownKeys = true }
                val accesoResult: AccesoLoginResult? = accesoResultJson?.let { json.decodeFromString(it) }

                Log.w("Exito", "Se obtuvo el perfil 2: ${accesoResult}")
                val alumnoAcademicoResultJson = Json.encodeToString(accesoResult)

                //VERIFICAR SI LAS CREDENCIALES INGRESADAS SON CORRECTAS
                if(accesoResult?.acceso==true){
                    Log.w("Exito", "Se obtuvo el perfil")
                    getAcademicProfile(context, navController,viewModel)
                    navController.navigate("data")
                }else{
                    showError(context, "ACCESO DENEGADO")
                }
            } else {
                showError(context, "Error en la autenticación. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeLogin>, t: Throwable){
            t.printStackTrace()
            showError(context, "Error en la solicitud")
        }
    })
}

private fun getAcademicProfile(context: Context, navController: NavController, viewModel: DataViewModel) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = profileRequestBody()
    service.getAcademicProfile(bodyProfile).enqueue(object : Callback<Envelope> {
        override fun onResponse(call: Call<Envelope>, response: Response<Envelope>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                val alumnoResultJson: String? = envelope?.body?.getAlumnoAcademicoWithLineamientoResponse?.getAlumnoAcademicoWithLineamientoResult

                // Deserializa la cadena JSON
                val json = Json { ignoreUnknownKeys = true }
                val alumnoAcademicoResult: AlumnoAcademicoResult? = alumnoResultJson?.let { json.decodeFromString(it) }

                Log.w("Exito", "Se obtuvo el perfil 2: ${alumnoAcademicoResult}")
                val alumnoAcademicoResultJson = Json.encodeToString(alumnoAcademicoResult)

                //BORRAR COOKIES DE SESION DESPUES DE UTILIZARLAS
                val addCookiesInterceptor = AddCookiesInterceptor(context)
                addCookiesInterceptor.clearCookies()

                //ALMACENAR Y MOSTRAR LA INFORMACION DEL ALUMNO
                viewModel.alumnoAcademicoResult=alumnoAcademicoResult
                navController.navigate("data")
            } else {
                showError(context, "Error al obtener el perfil académico. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<Envelope>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

//MENSAJES FLOTANTES
fun login(context: Context){
    Toast.makeText(context, "Iniciando sesión", Toast.LENGTH_SHORT).show()
}

private fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//BODYS DE PETICION
private fun loginRequestBody(matricula: String, contrasenia: String): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <accesoLogin xmlns="http://tempuri.org/">
              <strMatricula>$matricula</strMatricula>
              <strContrasenia>$contrasenia</strContrasenia>
              <tipoUsuario>ALUMNO</tipoUsuario>
            </accesoLogin>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun profileRequestBody(): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}