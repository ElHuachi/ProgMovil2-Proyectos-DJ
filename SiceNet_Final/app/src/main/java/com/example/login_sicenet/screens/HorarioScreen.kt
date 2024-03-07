package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.CargaAcademicaItemDB
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HorarioScreen (navController: NavController, viewModel: DataViewModel){
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = {
                    viewModel.setCargaAcResult(false)
                    navController.navigate("data_screen")
                }) {
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
                                DropdownMenuItem(text = { Text(text = "Información del alumno") }, onClick = {
                                    viewModel.setCargaAcResult(false)
                                    navController.navigate("data_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones parciales") }, onClick = {
                                    viewModel.setCargaAcResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.califUWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("calpar_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones finales") }, onClick = {
                                    viewModel.setCargaAcResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.califFWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("final_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Carga academica") }, onClick = {
                                    viewModel.setCargaAcResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.cargaAcWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("horario_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Kardex") }, onClick = {
                                    viewModel.setCargaAcResult(false)
                                    viewModel.kardexWorkManager(viewModel.nControl)
                                    navController.navigate("kardex_screen")
                                })
                            }
                        }
                    }
                }
            )
        }
    ) {
        BodyContentH(navController, viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyContentH(navController: NavController, viewModel: DataViewModel){
    val coroutineScope = rememberCoroutineScope()
    Box (modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFf5f5f5))
    ) {
        val context = LocalContext.current
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
            if(viewModel.internet==true){
                viewModel.getCargaAcademica()
                val cargaAc = viewModel.cargaAcademica
                // Verifica si alumnoAcademicoResult es null
                if (cargaAc != null) {
                    LazyColumn(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .padding(top = 60.dp)) {
                        items(cargaAc.size) { item ->
                            // Function to display each item
                            DisplayItemCargaAc(cargaAc[item])
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener la carga académica.")
                    }
                }
            }else{
                Log.d("obteniendo carga", "obteniendo carga")
                coroutineScope.launch {
                    Log.e("check","check")
                    viewModel.cargaAcDB=viewModel.getCargaAc(viewModel.nControl)
                }
                val cargaDB = viewModel.cargaAcDB
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (cargaDB != null && cargaDB.isNotEmpty()) {

                    LazyColumn (
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .padding(top = 60.dp)
                    ) {
                        items(cargaDB.size) { item ->
                            // Function to display each item
                            DisplayItemCargaAcOffline(cargaDB[item])
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Última actualización: ${cargaDB[0].fecha}",
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener la carga académica.", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayItemCargaAc(item: CargaAcademicaItem) {
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            colors = cardColors
        ) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Materia: ")
                }
                append(item.materia)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Grupo: ")
                }
                append(item.grupo)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Docente: ")
                }
                append(item.docente)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Lunes: ")
                }
                append(item.lunes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Martes: ")
                }
                append(item.martes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Miercoles: ")
                }
                append(item.miercoles)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Jueves: ")
                }
                append(item.jueves)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Viernes: ")
                }
                append(item.viernes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Sabado: ")
                }
                append(item.sabado)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Creditos: ")
                }
                append(item.creditosMateria.toString())
            }, color = Color.Black)
        }
    }
        Spacer(modifier = Modifier.height(2.dp))
    }

@Composable
fun DisplayItemCargaAcOffline(item: CargaAcademicaItemDB) {
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            colors = cardColors
        ) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Materia: ")
                }
                append(item.materia)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Grupo: ")
                }
                append(item.grupo)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Docente: ")
                }
                append(item.docente)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Lunes: ")
                }
                append(item.lunes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Martes: ")
                }
                append(item.martes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Miercoles: ")
                }
                append(item.miercoles)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Jueves: ")
                }
                append(item.jueves)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Viernes: ")
                }
                append(item.viernes)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sabado: ")
                }
                append(item.sabado)
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Creditos: ")
                }
                append(item.creditosMateria.toString())
            }, color = Color.Black)
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
}