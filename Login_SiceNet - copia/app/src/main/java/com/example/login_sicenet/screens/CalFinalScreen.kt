package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalFinalScreen(navController: NavController, viewModel: DataViewModel){
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = { navController.navigate("data_screen") }) {
                    Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio")
                }
                Text(text = "Calif. Finales",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 70.dp))
            },
                actions = {
                    Box(modifier = Modifier
                        .padding(8.dp)
                    ){
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(imageVector = Icons.Filled.DensityMedium, contentDescription = "Más Opciones",
                                modifier = Modifier
                                    .size(40.dp))
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(text = { Text(text = "Información del alumno") }, onClick = { navController.navigate("data_screen") })
                                DropdownMenuItem(text = { Text(text = "Calificaciones parciales") }, onClick = { navController.navigate("calpar_screen") })
                                DropdownMenuItem(text = { Text(text = "Calificaciones finales") }, onClick = { navController.navigate("final_screen") })
                                DropdownMenuItem(text = { Text(text = "Carga academica") }, onClick = { navController.navigate("horario_screen") })
                                DropdownMenuItem(text = { Text(text = "Kardex") }, onClick = { navController.navigate("kardex_screen") })
                            }
                        }
                    }
                }
            )
        }
    ) {
        BodyContentCF(viewModel)
       }
}


@Composable
fun BodyContentCF(viewModel: DataViewModel){
    val calF = viewModel.califFinales
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,

        )
    Image(painter = painterResource(id = R.drawable.backgrounddata), contentDescription = "Fondo de pantalla",
        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    if (calF != null){
        LazyColumn(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .padding(top = 60.dp)
        ){
            items(calF ?: emptyList()){Calificacion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    colors = cardColors
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Materia: ")
                            }
                            append(Calificacion.materia)
                            append(", ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Grupo: ")
                            }
                            append(Calificacion.grupo)
                            append(", ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Acreditacion: ")
                            }
                            append(Calificacion.acred)
                            append(", ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Calificación: ")
                            }
                            append(Calificacion.calif.toString())
                            append(", ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Observaciones: ")
                            }
                            append(Calificacion.observaciones)
                        },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}