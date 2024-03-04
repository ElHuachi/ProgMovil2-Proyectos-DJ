package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.model.CargaAcademica
import com.example.login_sicenet.model.CargaAcademicaItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HorarioScreen (navController: NavController, viewModel: DataViewModel){
    val cargaAcademica = viewModel.getCargaAcademica()
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = { navController.navigate("data") }) {
                    Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio")
                }
                Text(text = "Carga academica",
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
    }
    BodyContentH(viewModel)
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(10.dp)
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyContentH(viewModel: DataViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val column1Weight = .3f // 30%
    val column2Weight = .2f // 20%
    val column3Weight = .2f // 20%
    val horario = viewModel.cargaAcademica
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,

        )
//    if (viewModel.internet == true) {
        if (horario != null) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .padding(horizontal = 10.dp)
            ) {
                items(horario ?: emptyList()) { CargaAcademicaItem ->
                    Row(Modifier.background(Color.Gray)) {
                        TableCell(text = "Materia", weight = column1Weight)
                        TableCell(text = "Docente", weight = column2Weight)
                        TableCell(text = "Lunes", weight = column2Weight)
                        TableCell(text = "Martes", weight = column2Weight)
                        TableCell(text = "Miércoles", weight = column2Weight)
                        TableCell(text = "Jueves", weight = column2Weight)
                        TableCell(text = "Viernes", weight = column2Weight)
                    }
                    Row {
                        TableCell(text = CargaAcademicaItem.materia, weight = column1Weight)
                        TableCell(text = CargaAcademicaItem.docente, weight = column2Weight)
                        TableCell(
                            text = CargaAcademicaItem.lunes.toString(),
                            weight = column3Weight
                        )
                        TableCell(
                            text = CargaAcademicaItem.martes.toString(),
                            weight = column3Weight
                        )
                        TableCell(
                            text = CargaAcademicaItem.miercoles.toString(),
                            weight = column3Weight
                        )
                        TableCell(
                            text = CargaAcademicaItem.jueves.toString(),
                            weight = column3Weight
                        )
                        TableCell(
                            text = CargaAcademicaItem.viernes.toString(),
                            weight = column3Weight
                        )
                    }
                }
            }
        }
//            coroutineScope.launch {
//                val existente = viewModel.getCaliUnidadExistente(viewModel.nControl)
//                if (existente == true) {
//                    Log.e("HorarioScreen", "Carga academica existente")
//                } else{
//                    viewModel.saveCargaAc()
//                }
//            }
//        } else {
//            Column (modifier = Modifier.padding(16.dp)) {
//                Text(text = "No hay carga academica disponible")
//            }
//        }
//    } else {
//        Log.d("Obteniendo Carga Academica", "Obteniendo Carga Academica")
//        coroutineScope.launch {
//            Log.e("check","check")
//            viewModel.cargaAcademica = viewModel.getCargaAcademica(viewModel.nControl)
//        }
//    }
}