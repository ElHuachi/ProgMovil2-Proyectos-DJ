package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.media.Image
import android.webkit.CookieManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.data.AppContainer
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.navigation.AppScreens
import com.example.login_sicenet.ui.theme.Green80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DataScreen(navController: NavController, viewModel: DataViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Perfil Académico",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
                                contentDescription = "SiceNet Logo",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }
                )
            }
        ) {
            BodyContent(navController, viewModel)
        }
}


@Composable
fun BodyContent(navController: NavController, viewModel: DataViewModel) {
    Box (modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFf5f5f5))
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgrounddata),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val alumnoAcademicoResult = viewModel.alumnoAcademicoResult
            // Verifica si alumnoAcademicoResult es null
            if (alumnoAcademicoResult != null) {
                val dataMap = mapOf(
                    "Nombre" to alumnoAcademicoResult.nombre,
                    "Matrícula" to alumnoAcademicoResult.matricula,
                    "Carrera" to alumnoAcademicoResult.carrera,
                    "Fecha de reinscripción" to alumnoAcademicoResult.fechaReins,
                    "Modalidad educativa" to alumnoAcademicoResult.modEducativo.toString(),
                    "Adeudo" to alumnoAcademicoResult.adeudo.toString(),
                    "Inscrito" to alumnoAcademicoResult.inscrito.toString(),
                    "Estatus" to alumnoAcademicoResult.estatus,
                    "Semestre actual" to alumnoAcademicoResult.semActual.toString(),
                    "Créditos acumulados" to alumnoAcademicoResult.cdtosAcumulados.toString(),
                    "Créditos actuales" to alumnoAcademicoResult.cdtosActuales.toString(),
                    "Especialidad" to alumnoAcademicoResult.especialidad
                )

                val cardColors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00BFA5),
                    contentColor = Color.Black,

                    )
                Column(modifier = Modifier.padding(16.dp)) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nombre",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.nombre}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Matrícula",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.matricula}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Carrera",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.carrera}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Especialidad",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.especialidad}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
//                Spacer(modifier = Modifier.height(10.dp))
//                Card(shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth(), colors = cardColors) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                            .sizeIn(minHeight = 40.dp)
//                    ) {
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(text = "Inscrito",
//                                style = MaterialTheme.typography.titleLarge,
//                                modifier = Modifier.background(color = Color(0x00000000))
//                            )
//                            var texto = ""
//                            if(alumnoAcademicoResult.inscrito) texto="SI" else texto="NO"
//                            Text(text = texto,
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
//                    }
//                }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Semestre actual",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.semActual}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Créditos acumulados",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.cdtosAcumulados}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = cardColors
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .sizeIn(minHeight = 40.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Créditos actuales",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.background(color = Color(0x00000000))
                                )
                                Text(
                                    text = "${alumnoAcademicoResult.cdtosActuales}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "No se pudo obtener el perfil académico.")
                    Text(text = "Revisa tus credenciales de inicio de sesión.")
                }
            }
            Button(onClick = {
                // Navegar a la pantalla de login
                navController.navigateUp()
            }) {
                Text(text = "Salir")
            }
        }
    }
}
