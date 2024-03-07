package com.example.login_sicenet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AboutScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Creado por: \n\n- David Díaz Jacobo\n- Juan José Arreola Gonzalez\n\nGrupo: TI5B\nCarrera: Sistemas Computacionales",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            BackButton(navController)
        }
    }

}

@Composable
fun BackButton(navController: NavController) {
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { navController.popBackStack() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(text = "Atras")
    }
}