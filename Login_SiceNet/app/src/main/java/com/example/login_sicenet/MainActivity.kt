package com.example.login_sicenet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Xml
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.ui.theme.Login_SiceNetTheme
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import org.xmlpull.v1.XmlPullParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login_SiceNetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun LoginScreen(){
        val context = LocalContext.current

        var user by remember { mutableStateOf("") }
        var pass by remember {
            mutableStateOf("")
        }
        var isValidUser by remember {
            mutableStateOf(false)
        }
        var isValidPass by remember {
            mutableStateOf(false)
        }
        var passVisible by remember {
            mutableStateOf(false)
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFb5e48c))
        ) {
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
                        RowImage()
                        RowUser(
                            user = user,
                            userChange = {
                                user = it
                                isValidUser = it.length >= 9
                            },
                            isValidUser = isValidUser
                        )
                        RowPass(
                            pass = pass,
                            passChange = {
                                pass = it
                                isValidPass = pass.length >= 6
                            },
                            passVisible = passVisible,
                            passVisibleChange = {
                                passVisible = !passVisible
                            },
                            isValidPass = isValidPass
                        )
                        RowButtonLogin(
                            context = context,
                            isValidUser = isValidUser,
                            isValidPass = isValidPass,
                            nControl = user,
                            password = pass
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RowImage(){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(modifier = Modifier.width(100.dp),
                painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
                contentDescription = "Imagen de Login")

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
                    unfocusedIndicatorColor = if (isValidPass) Color.Green else Color.Red
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
        password: String
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
                },
                enabled = isValidUser && isValidPass) {
                Text("Iniciar Sesión")
            }
        }
    }

    fun login(context: Context){
        Toast.makeText(context, "Iniciando sesión", Toast.LENGTH_SHORT).show()
    }

    private fun loginRequestBody(matricula: String, contrasenia: String): RequestBody {
        return RequestBody.create(
            "text/xml".toMediaTypeOrNull(), """
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <accesoLogin xmlns="http://tempuri.org/">
                  <strMatricula>$matricula</strMatricula>
                  <strContrasenia>$contrasenia</strContrasenia>
                  <tipoUsuario>ALUMNO</tipoUsuario>
                </accesoLogin>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent())
    }

    private fun profileRequestBody(): RequestBody {
        return RequestBody.create(
            "text/xml".toMediaTypeOrNull(), """
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
              </soap:Body>
            </soap:Envelope>
        """.trimIndent())
    }

}
