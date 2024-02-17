package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DataScreen(navController: NavController, viewModel: DataViewModel) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = "SiceNet Data Screen")
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                actions = {
//                    Box(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .clickable { /* Agregar lógica si se desea hacer clic en el logo */ }
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
//                            contentDescription = "SiceNet Logo",
//                            modifier = Modifier
//                                .size(40.dp)
//                        )
//                    }
//                }
//            )
//        }
//    ) {
        //BodyContent(navController)
        val alumnoAcademicoResult = viewModel.alumnoAcademicoResult
        // Verifica si alumnoAcademicoResult es null
        if (alumnoAcademicoResult != null) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nombre: ${alumnoAcademicoResult.nombre}")
                Text(text = "Matrícula: ${alumnoAcademicoResult.matricula}")
                Text(text = "Carrera: ${alumnoAcademicoResult.carrera}")
                // Agrega más campos aquí
            }
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "No se pudo obtener el perfil académico.")
            }
        }
}


@Composable
fun BodyContent(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Data Screen")
        Button(onClick = { navController.navigate(AppScreens.LoginScreen.route) }) {
            Text(text = "Cerrar sesion")
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DataScreenPreview() {
//    DataScreen(navController = NavController(AppScreens.DataScreen.route))
//}