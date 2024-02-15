package com.example.login_sicenet

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                        isValidPass = isValidPass
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
    isValidPass: Boolean
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = { login(context) },
            enabled = isValidUser && isValidPass) {
            Text("Iniciar Sesión")
        }
    }
}

fun login(context: Context){
    Toast.makeText(context, "Iniciando sesión", Toast.LENGTH_SHORT).show()
}