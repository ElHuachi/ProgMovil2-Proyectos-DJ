package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.Button
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
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CalificacionUnidad
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.network.AddCookiesInterceptor
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalParScreen(navController: NavController, viewModel: DataViewModel){
    var expanded by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBar(title = {
                IconButton(onClick = { navController.navigate("data_screen") }) {
                    Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio")
                }
                Text(text = "Calif. Parciales",
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
        // Adjust the padding for the content to prevent it from being hidden by the top app bar
        BodyContentCalif(viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun BodyContentCalif(viewModel: DataViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Image(painter = painterResource(id = R.drawable.backgrounddata), contentDescription = "Fondo de pantalla",
        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.internet) {
            //viewModel.getCalifUnidades()
            val calif = viewModel.califUnidades
            // Verifica si alumnoAcademicoResult es null
            if (calif != null) {
                LazyColumn {
                    items(calif.size) { item ->
                        // Function to display each item
                        DisplayItem(calif[item])
                    }
                }
                coroutineScope.launch {
                    val existente = viewModel.getCaliUnidadExistente(viewModel.nControl)
                    if (existente == true) {
                        Log.e("ya estan", "ya estan")

                    } else {
                        viewModel.saveCaliUnidad()

                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "No se pudieron obtener las calificaciones")
                }
            }
        } else {
            Log.d("obteniendo perfil", "obteniendo perfil")
            coroutineScope.launch {
                Log.e("check", "check")
                viewModel.caliUnidadDB1 = viewModel.getCaliUnidad1(viewModel.nControl)

                //viewModel.deleteAccessDB("S20120179")
            }
            val caliDB = viewModel.caliUnidadDB1
            //PANTALLA LLENADA DESDE LA BASE DE DATOS
            if (caliDB != null) {

                LazyColumn (
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .padding(top = 60.dp)
                ) {
                    items(1) { item ->
                        DisplayItemDB(caliDB)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Última actulizacíon: ${caliDB?.fecha}", color = Color.White)
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "No se pudo obtener el perfil académico.", color = Color.White)
                }
            }
        }
    }
}
@Composable
fun DisplayItem(item: CalificacionUnidad) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Itera sobre las unidades y crea un solo Text con unidades y calificaciones
        if (item.unidadesActivas.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF009688),
                    contentColor = Color.Black,
                )
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Materia: ")
                    }
                    append(item.materia)
                    append("  ") // Agrega un espacio entre Materia y Grupo
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Grupo: ")
                    }
                    append(item.grupo)
                })
                Text(
                    text = buildAnnotatedString {
                        for (i in item.unidadesActivas.indices) {
                            if (i > 0) {
                                append("  ") // Agrega un espacio entre unidades
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("U${i+1}: ")
                            }
                            append("${getCalificacion(item, i)}")
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Función para obtener la calificación de la unidad específica
private fun getCalificacion(item: CalificacionUnidad, unidadIndex: Int): String? {
    return when (unidadIndex) {
        0 -> item.c1
        1 -> item.c2
        2 -> item.c3
        3 -> item.c4
        4 -> item.c5
        5 -> item.c6
        6 -> item.c7
        7 -> item.c8
        8 -> item.c9
        9 -> item.c10
        10 -> item.c11
        11 -> item.c12
        12 -> item.c13
        else -> ""
    }
}

private fun getCalificacionDB(item: CalificacionUnidadDB, unidadIndex: Int): String? {
    return when (unidadIndex) {
        0 -> item.c1
        1 -> item.c2
        2 -> item.c3
        3 -> item.c4
        4 -> item.c5
        5 -> item.c6
        6 -> item.c7
        7 -> item.c8
        8 -> item.c9
        9 -> item.c10
        10 -> item.c11
        11 -> item.c12
        12 -> item.c13
        else -> ""
    }
}

@Composable
fun DisplayItemDB(item: CalificacionUnidadDB) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFA7C58D),
                contentColor = Color.Black,
            )
        ) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Materia: ")
                }
                append(item.materia)
                append("  ") // Agrega un espacio entre Materia y Grupo
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Grupo: ")
                }
                append(item.grupo)
            })
            Text(
                text = buildAnnotatedString {
                    for (i in item.unidadesActivas.indices) {
                        if (i > 0) {
                            append("  ") // Agrega un espacio entre unidades
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("U${i + 1}: ")
                        }
                        append("${getCalificacionDB(item, i)}")
                    }
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}