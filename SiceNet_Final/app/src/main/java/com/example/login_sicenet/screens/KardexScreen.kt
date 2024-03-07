package com.exampl

import com.example.login_sicenet.screens.DataViewModel
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItem
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.Promedio
import com.example.login_sicenet.model.PromedioDB
import com.example.login_sicenet.screens.DisplayItemCargaAcOffline
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KardexScreen (navController: NavController, viewModel: DataViewModel){
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = {
                    viewModel.setKardexResult(false)
                    navController.navigate("data_screen")
                }) {
                    Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio")
                }
                Text(text = "Kardex",
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
                                    viewModel.setKardexResult(false)
                                    navController.navigate("data_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones parciales") }, onClick = {
                                    viewModel.setKardexResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.califUWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("calpar_screen")

                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones finales") }, onClick = {
                                    viewModel.setKardexResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.califFWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("final_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Carga academica") }, onClick = {
                                    viewModel.setKardexResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.cargaAcWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("horario_screen")

                                })
                                DropdownMenuItem(text = { Text(text = "Kardex") }, onClick = {
                                    viewModel.setKardexResult(false)
                                    if(viewModel.internet==true){
                                        viewModel.kardexWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("kardex_screen")
                                })
                            }
                        }
                    }
                }
            )
        }
    ) {
        BodyKardex(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyKardex(viewModel: DataViewModel){
    val coroutineScope = rememberCoroutineScope()
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,
    )
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
                viewModel.getKardex()
                val kardex = viewModel.kardex?.lstKardex
                val promedio = viewModel.kardex?.promedio
                // Verifica si alumnoAcademicoResult es null
                if (kardex != null && promedio != null) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .padding(top = 60.dp)
                    ) {
                        items(kardex.size) { item ->
                            // Function to display each item
                            DisplayItemKardex(kardex[item])
                        }
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = cardColors
                    ){
                        Column (modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            Text(text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("PROMEDIO")
                                }
                                append("\n")
                                append("Promedio General: ${promedio.promedioGral}")
                                append("\n")
                                append("Creditos Acumulados: ${promedio.cdtsAcum}")
                                append("\n")
                                append("Creditos Totales: ${promedio.cdtsPlan}")
                                append("\n")
                                append("Avance de Craditos: ${promedio.avanceCdts}%")
                                append("\n")
                                append("Materias Cursadas: ${promedio.matCursadas}")
                                append("\n")
                                append("Materias Aprobadas: ${promedio.matAprobadas}")

                            })
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener el kardex.")
                    }
                }
            }else{
                Log.d("obteniendo carga", "Obteniendo carga")
                coroutineScope.launch {
                    Log.e("check","check")
                    viewModel.kardexDB=viewModel.getKardex(viewModel.nControl)
                    viewModel.promedioDB1=viewModel.getPromedio1(viewModel.nControl)
                }
                val kardexDB = viewModel.kardexDB
                val promedioDB = viewModel.promedioDB1
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (kardexDB != null) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .padding(top = 60.dp)
                    ) {
                        items(kardexDB.size) { item ->
                            // Function to display each item
                            DisplayItemKardexOffline(kardexDB[item])
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("PROMEDIO")
                                    }
                                    append("\n")
                                    append("Promedio General: ${promedioDB?.promedioGral}")
                                    append("\n")
                                    append("Creditos Acumulados: ${promedioDB?.cdtsAcum}")
                                    append("\n")
                                    append("Creditos Totales: ${promedioDB?.cdtsPlan}")
                                    append("\n")
                                    append("Avance de Craditos: ${promedioDB?.avanceCdts}%")
                                    append("\n")
                                    append("Materias Cursadas: ${promedioDB?.matCursadas}")
                                    append("\n")
                                    append("Materias Aprobadas: ${promedioDB?.matAprobadas}")
                                }, color = Color.Black)
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
fun DisplayItemKardex(item: KardexItem) {
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,
        )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = cardColors
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Clave Materia: ")
            }
            append(item.clvMat)
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Clave Oficial: ")
            }
            append(item.clvOfiMat)
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Materia: ")
            }
            append(item.materia)
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Creditos: ")
            }
            append(item.cdts.toString())
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Calificación: ")
            }
            append(item.calif.toString())
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Acreditación: ")
            }
            append(item.acred)
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Semestre: ")
            }
            if(item.s1?.length!! >0){
                append(item.s1)
            }else if(item.s2?.length!! >0){
                append(item.s2)
            }
            else if(item.s3?.length!! >0){
                append(item.s3)
            }
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Periodo: ")
            }
            if(item.p1?.length!! >0){
                append(item.p1)
            }else if(item.p2?.length!! >0){
                append(item.p2)
            }
            else if(item.p3?.length!! >0){
                append(item.p3)
            }
            append(", ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Año: ")
            }
            if(item.a1?.length!! >0){
                append(item.a1)
            }else if(item.a2?.length!! >0){
                append(item.a2)
            }
            else if(item.a3?.length!! >0){
                append(item.a3)
            }
        }, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
    }
}}

@Composable
fun DisplayItemKardexOffline(item: KardexItemDB) {
    val cardColors = CardDefaults.cardColors(
        containerColor = Color(0xFF76FF03),
        contentColor = Color.Black,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = cardColors
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Clave Materia: ")
                }
                append(item.clvMat)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Clave Oficial: ")
                }
                append(item.clvOfiMat)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Materia: ")
                }
                append(item.materia)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Creditos: ")
                }
                append(item.cdts.toString())
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Calificación: ")
                }
                append(item.calif.toString())
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Acreditación: ")
                }
                append(item.acred)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Semestre: ")
                }
                if (item.s1?.length!! > 0) {
                    append(item.s1)
                } else if (item.s2?.length!! > 0) {
                    append(item.s2)
                } else if (item.s3?.length!! > 0) {
                    append(item.s3)
                }
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Periodo: ")
                }
                if (item.p1?.length!! > 0) {
                    append(item.p1)
                } else if (item.p2?.length!! > 0) {
                    append(item.p2)
                } else if (item.p3?.length!! > 0) {
                    append(item.p3)
                }
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Año: ")
                }
                if (item.a1?.length!! > 0) {
                    append(item.a1)
                } else if (item.a2?.length!! > 0) {
                    append(item.a2)
                } else if (item.a3?.length!! > 0) {
                    append(item.a3)
                }
            }, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}