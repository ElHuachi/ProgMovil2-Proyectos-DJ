package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
                                DropdownMenuItem(text = { Text(text = "Información del alumno") }, onClick = { navController.navigate("data") })
                                DropdownMenuItem(text = { Text(text = "Calificaciones parciales") }, onClick = { navController.navigate("calpar_screen") })
                                DropdownMenuItem(text = { Text(text = "Calificaciones finales") }, onClick = { navController.navigate("final_screen") })
                                DropdownMenuItem(text = { Text(text = "Carga academica") }, onClick = { navController.navigate("horario_screen") })
                            }
                        }
                    }
                }
            )
        }
    ) {innerPadding ->
        BodyContentH(navController, viewModel, Modifier.padding(top = innerPadding.calculateTopPadding()))
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

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyContentH(navController: NavController, viewModel: DataViewModel, Modifier: Modifier){

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
                //viewModel.getCalifUnidades()
                val cargaAc = viewModel.cargaAcademica
                // Verifica si alumnoAcademicoResult es null
                if (cargaAc != null) {
                    LazyColumn {
                        items(cargaAc.size) { item ->
                            // Function to display each item
                            DisplayItemCargaAc(cargaAc[item])
                        }
                    }
                    coroutineScope.launch {
                        val existente = viewModel.getCaliFinalExistente(viewModel.nControl)
                        if(existente==true){

                            //viewModel.updateCargaAc()
                            viewModel.saveCargaAc()
                        }else{

                            Log.e("ya estan", "ya estan")
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener la carga académica.")
                    }
                }
            }else{
                Log.d("obteniendo perfil", "obteniendo perfil")
                coroutineScope.launch {
                    Log.e("check","check")
                    viewModel.cargaAcDB1=viewModel.getCargaAcademica1(viewModel.nControl)

                    //viewModel.deleteAccessDB("S20120179")
                }
                val cargaDB = viewModel.cargaAcDB1
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (cargaDB != null) {

                    LazyColumn {
                        items(1) { item ->
                            // Function to display each item
                            DisplayItemCargaAcOffline(cargaDB)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Última actulizacíon: ${cargaDB?.fecha}", color = Color.White)
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Grupo: ${item.grupo}", color = Color.White)
        Text(text = "Docente: ${item.docente}", color = Color.White)
        Text(text = "Lunes: ${item.lunes}", color = Color.White)
        Text(text = "Martes: ${item.martes}", color = Color.White)
        Text(text = "Miercoles: ${item.miercoles}", color = Color.White)
        Text(text = "Jueves: ${item.jueves}", color = Color.White)
        Text(text = "Viernes: ${item.viernes}", color = Color.White)
        Text(text = "Sabado: ${item.sabado}", color = Color.White)
        Text(text = "Creditos: ${item.creditosMateria}", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DisplayItemCargaAcOffline(item: CargaAcademicaItemDB) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Grupo: ${item.grupo}", color = Color.White)
        Text(text = "Docente: ${item.docente}", color = Color.White)
        Text(text = "Lunes: ${item.lunes}", color = Color.White)
        Text(text = "Martes: ${item.martes}", color = Color.White)
        Text(text = "Miercoles: ${item.miercoles}", color = Color.White)
        Text(text = "Jueves: ${item.jueves}", color = Color.White)
        Text(text = "Viernes: ${item.viernes}", color = Color.White)
        Text(text = "Sabado: ${item.sabado}", color = Color.White)
        Text(text = "Creditos: ${item.creditosMateria}", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
    }
}