package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.model.CargaAcademica
import com.example.login_sicenet.model.CargaAcademicaItem

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
                            }
                        }
                    }
                }
            )
        }
    ) {
    }
    BodyContentH(cargaAcademica = CargaAcademica(
        lstCargaAcademica = listOf(
            CargaAcademicaItem(
                semipresencial = "semipresencial",
                observaciones = "observaciones",
                docente = "docente",
                clvOficial = "clvOficial",
                sabado = "sabado",
                viernes = "viernes",
                jueves = "jueves",
                miercoles = "miercoles",
                martes = "martes",
                lunes = "lunes",
                estadoMateria = "estadoMateria",
                creditosMateria = 5,
                materia = "materia",
                grupo = "grupo"
            )
        )
    ))
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

@Composable
fun BodyContentH(cargaAcademica: CargaAcademica) {
    // Each cell of a column must have the same weight.
    val column1Weight = .3f // 30%
    val column2Weight = .2f // 20%
    val column3Weight = .2f // 20%
    val column4Weight = .2f // 20%
    val column5Weight = .2f // 20%

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
            .padding(horizontal = 10.dp)
    ) {
        // Here is the header
        item {
            Row(Modifier.background(Color.Gray)) {
                TableCell(text = "Materia", weight = column1Weight)
                TableCell(text = "Docente", weight = column5Weight)
                TableCell(text = "Lunes", weight = column2Weight)
                TableCell(text = "Martes", weight = column3Weight)
                TableCell(text = "Miércoles", weight = column4Weight)
                TableCell(text = "Jueves", weight = column5Weight)
                TableCell(text = "Viernes", weight = column5Weight)
            }
        }

        // Table data
        item {
            LazyRow {
                items(cargaAcademica.lstCargaAcademica) { item ->
                    Row(Modifier.fillMaxWidth()) {
                        TableCell(text = item.materia, weight = column1Weight)
                        TableCell(text = item.docente, weight = column3Weight)
                        TableCell(text = item.lunes ?: "", weight = column3Weight)
                        TableCell(text = item.martes ?: "", weight = column4Weight)
                        TableCell(text = item.miercoles ?: "", weight = column5Weight)
                        TableCell(text = item.jueves ?: "", weight = column5Weight)
                        TableCell(text = item.viernes ?: "", weight = column5Weight)
                        TableCell(text = item.sabado ?: "", weight = column5Weight)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBodyContentH() {
    val cargaAcademica = CargaAcademica(
        lstCargaAcademica = listOf(
            CargaAcademicaItem(
                semipresencial = "semipresencial",
                observaciones = "observaciones",
                docente = "docente",
                clvOficial = "clvOficial",
                sabado = "sabado",
                viernes = "viernes",
                jueves = "jueves",
                miercoles = "miercoles",
                martes = "martes",
                lunes = "lunes",
                estadoMateria = "estadoMateria",
                creditosMateria = 5,
                materia = "materia",
                grupo = "grupo"
            )
        )
    )
    BodyContentH(cargaAcademica = cargaAcademica)
}
