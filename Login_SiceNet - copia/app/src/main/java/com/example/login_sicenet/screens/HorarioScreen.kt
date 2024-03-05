package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.TableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HorarioScreen(navController: NavController, viewModel: DataViewModel) {
    val cargaAcademica = viewModel.getCargaAcademica()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = { navController.navigate("data_screen") }) {
                    Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio")
                }
                Text(
                    text = "Carga academica",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 70.dp)
                )
            },
                actions = {
                    Box(modifier = Modifier.padding(8.dp)) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.DensityMedium,
                                contentDescription = "Más Opciones",
                                modifier = Modifier.size(40.dp)
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = "Información del alumno") },
                                    onClick = { navController.navigate("data_screen") })
                                DropdownMenuItem(
                                    text = { Text(text = "Calificaciones parciales") },
                                    onClick = { navController.navigate("calpar_screen") })
                                DropdownMenuItem(
                                    text = { Text(text = "Calificaciones finales") },
                                    onClick = { navController.navigate("final_screen") })
                                DropdownMenuItem(
                                    text = { Text(text = "Carga academica") },
                                    onClick = { navController.navigate("horario_screen") })
                                DropdownMenuItem(
                                    text = { Text(text = "Kardex") },
                                    onClick = { navController.navigate("kardex_screen") })
                            }
                        }
                    }
                }
            )
        }
    ) {
        BodyContentH(viewModel)
    }
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

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun BodyContentH(viewModel: DataViewModel) {
    val column1Weight = .3f // 30%
    val column2Weight = .2f // 20%
    val column3Weight = .2f // 20%
    val horario = viewModel.cargaAcademica
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isWideEnough = isScreenWideEnoughForRow(configuration)

    if (horario != null) {
        if (isPortrait && isWideEnough) {
            LazyRow(
                Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .padding(horizontal = 10.dp)
            ) {
                item {
                    // header
                    Row(modifier = Modifier
                        .background(Color.Gray)
                    ) {
                        TableCell(text = "Materia", weight = column1Weight)
                        TableCell(text = "Docente", weight = column2Weight)
                        TableCell(text = "Lunes", weight = column2Weight)
                        TableCell(text = "Martes", weight = column2Weight)
                        TableCell(text = "Miércoles", weight = column2Weight)
                        TableCell(text = "Jueves", weight = column2Weight)
                        TableCell(text = "Viernes", weight = column2Weight)
                    }
                }
                items(horario) { CargaAcademicaItem ->
                    // Content rows
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
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .padding(horizontal = 10.dp)
            ) {
                item {
                    // header
                    Row(modifier = Modifier.background(Color.Gray)) {
                        TableCell(text = "Materia", weight = column1Weight)
                        TableCell(text = "Docente", weight = column2Weight)
                        TableCell(text = "Lunes", weight = column2Weight)
                        TableCell(text = "Martes", weight = column2Weight)
                        TableCell(text = "Miércoles", weight = column2Weight)
                        TableCell(text = "Jueves", weight = column2Weight)
                        TableCell(text = "Viernes", weight = column2Weight)
                    }
                }
                items(horario) { CargaAcademicaItem ->
                    // Content rows
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
    }
}

@Composable
private fun isScreenWideEnoughForRow(configuration: Configuration): Boolean {
    return configuration.screenWidthDp >= 600
}