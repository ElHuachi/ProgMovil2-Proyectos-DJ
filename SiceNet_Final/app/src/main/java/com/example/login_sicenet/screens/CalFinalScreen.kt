package com.example.login_sicenet.screens

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
import androidx.compose.foundation.lazy.items
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
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidad
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalFinalScreen(navController: NavController, viewModel: DataViewModel){
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = {
                    viewModel.setCalifFResult(false)
                    navController.navigate("data_screen")
                }) {
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
                                DropdownMenuItem(text = { Text(text = "Información del alumno") }, onClick = {
                                    viewModel.setCalifFResult(false)
                                    navController.navigate("data_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones parciales") }, onClick = {
                                    viewModel.setCalifFResult(false)
                                    if(viewModel.checkInternetConnection(context)){
                                        viewModel.califUWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("calpar_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Calificaciones finales") }, onClick = {
                                    viewModel.setCalifFResult(false)
                                    if(viewModel.checkInternetConnection(context)){
                                        viewModel.califFWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("final_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Carga academica") }, onClick = {
                                    viewModel.setCalifFResult(false)
                                    if(viewModel.checkInternetConnection(context)){
                                        viewModel.cargaAcWorkManager(viewModel.nControl)
                                    }
                                    navController.navigate("horario_screen")
                                })
                                DropdownMenuItem(text = { Text(text = "Kardex") }, onClick = {
                                    viewModel.setCalifFResult(false)
                                    if(viewModel.checkInternetConnection(context)){
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
        BodyContentCF(navController, viewModel)
       }
}

@Composable
fun RowScope.TableCellCF(
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


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyContentCF(navController: NavController, viewModel: DataViewModel){
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
            if(viewModel.checkInternetConnection(context)){
                viewModel.getCalifFinales()
                val calif = viewModel.califFinales
                // Verifica si alumnoAcademicoResult es null
                if (calif != null) {
                    LazyColumn(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .padding(top = 60.dp)) {
                        items(calif.size) { item ->
                            // Function to display each item
                            DisplayItemFinales(calif[item])
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudieron obtener las calificaciones")
                    }
                }
            }else{
                Log.d("obteniendo perfil", "obteniendo perfil")
                coroutineScope.launch {
                    Log.e("check","check")
                    viewModel.caliFinalDB=viewModel.getCaliFinal(viewModel.nControl)
                }
                val caliDB = viewModel.caliFinalDB
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (caliDB != null && caliDB.isNotEmpty()) {

                    LazyColumn(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .padding(top = 60.dp)) {
                        items(caliDB.size) { item ->
                            // Function to display each item
                            DisplayItemFinalesOffline(caliDB[item])
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Última actualización: ${caliDB[0].fecha}",
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudieron obtener las calificaciones.", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayItemFinales(item: Calificacion) {
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
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Grupo: ")
                }
                append(item.grupo)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Modalidad: ")
                }
                append(item.acred)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Calificación: ")
                }
                append(item.calif.toString())
            },
                modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
}

@Composable
fun DisplayItemFinalesOffline(item: CalificacionDB) {
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
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Grupo: ")
                }
                append(item.grupo)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Modalidad: ")
                }
                append(item.acred)
                append(", ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Calificación: ")
                }
                append(item.calif.toString())
            },
                modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
}