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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItem
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.Promedio
import com.example.login_sicenet.model.PromedioDB
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
                IconButton(onClick = { navController.navigate("data") }) {
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
                                    navController.navigate("data")
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
    ) {innerPadding ->
        BodyKardex(viewModel, Modifier.padding(top = innerPadding.calculateTopPadding()))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyKardex(viewModel: DataViewModel, Modifier: Modifier){

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
                viewModel.getKardex()
                val kardex = viewModel.kardex?.lstKardex
                val promedio = viewModel.kardex?.promedio
                // Verifica si alumnoAcademicoResult es null
                if (kardex != null && promedio != null) {
                    LazyColumn {
                        items(kardex.size) { item ->
                            // Function to display each item
                            DisplayItemKardex(kardex[item], promedio)
                        }
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                    Text(text = "PROMEDIO", color = Color.White)
                    Text(text = "Promedio General: ${promedio.promedioGral}", color = Color.White)
                    Text(text = "Creditos Acumulados: ${promedio.cdtsAcum}", color = Color.White)
                    Text(text = "Creditos Totales: ${promedio.cdtsPlan}", color = Color.White)
                    Text(text = "Avance de Craditos: ${promedio.avanceCdts}%", color = Color.White)
                    Text(text = "Materias Cursadas: ${promedio.matCursadas}", color = Color.White)
                    Text(text = "Materias Aprobadas: ${promedio.matAprobadas}", color = Color.White)
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener el kardex.")
                    }
                }
            }else{
                Log.d("obteniendo carga", "obteniendo carga")
                coroutineScope.launch {
                    Log.e("check","check")
                    viewModel.kardexDB1=viewModel.getKardex1(viewModel.nControl)
                    viewModel.promedioDB1=viewModel.getPromedio1(viewModel.nControl)

                    //viewModel.deleteAccessDB("S20120179")
                }
                val kardexDB = viewModel.kardexDB1
                val promedioDB = viewModel.promedioDB1
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (kardexDB != null) {

//                    LazyColumn {
//                        items(1) { item ->
//                            // Function to display each item
//                            if (promedioDB != null) {
//                                DisplayItemKardexOffline(kardexDB, promedioDB)
//                            }
//                        }
//                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                    Text(text = "PROMEDIO", color = Color.White)
                    Text(text = "Promedio General: ${promedioDB?.promedioGral}", color = Color.White)
                    Text(text = "Creditos Acumulados: ${promedioDB?.cdtsAcum}", color = Color.White)
                    Text(text = "Creditos Totales: ${promedioDB?.cdtsPlan}", color = Color.White)
                    Text(text = "Avance de Craditos: ${promedioDB?.avanceCdts}%", color = Color.White)
                    Text(text = "Materias Cursadas: ${promedioDB?.matCursadas}", color = Color.White)
                    Text(text = "Materias Aprobadas: ${promedioDB?.matAprobadas}", color = Color.White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Última actulizacíon: ${promedioDB?.fecha}", color = Color.White)
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
fun DisplayItemKardex(item: KardexItem, promedio: Promedio) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "CVE: ${item.clvMat}", color = Color.White)
        Text(text = "CVE Oficial: ${item.clvOfiMat}", color = Color.White)
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Creditos: ${item.cdts}", color = Color.White)
        Text(text = "Calificación: ${item.calif}", color = Color.White)
        Text(text = "Acreditación: ${item.acred}", color = Color.White)
        if(item.s1?.length!! >0){
            Text(text = "Semestre: ${item.s1}", color = Color.White)
        }else if(item.s2?.length!! >0){
            Text(text = "Semestre: ${item.s2}", color = Color.White)
        }
        else if(item.s3?.length!! >0){
            Text(text = "Semestre: ${item.s3}", color = Color.White)
        }
        if(item.p1?.length!!>0){
            Text(text = "Periodo: ${item.p1}", color = Color.White)
        }else if(item.p2?.length!! >0){
            Text(text = "Periodo: ${item.p2}", color = Color.White)
        }
        else if(item.p3?.length!! >0){
            Text(text = "Periodo: ${item.p3}", color = Color.White)
        }
        if(item.a1?.length!!>0){
            Text(text = "Año: ${item.a1}", color = Color.White)
        }else if(item.a2?.length!! >0){
            Text(text = "Año: ${item.a2}", color = Color.White)
        }
        else if(item.a3?.length!! >0){
            Text(text = "Año: ${item.a3}", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DisplayItemKardexOffline(item: KardexItemDB, promedio: PromedioDB) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "CVE: ${item.clvMat}", color = Color.White)
        Text(text = "CVE Oficial: ${item.clvOfiMat}", color = Color.White)
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Creditos: ${item.cdts}", color = Color.White)
        Text(text = "Calificación: ${item.calif}", color = Color.White)
        Text(text = "Acreditación: ${item.acred}", color = Color.White)
        if(item.s1?.length!!>0){
            Text(text = "Semestre: ${item.s1}", color = Color.White)
        }else if(item.s2?.length!!>0){
            Text(text = "Semestre: ${item.s2}", color = Color.White)
        }
        else if(item.s3?.length!!>0){
            Text(text = "Semestre: ${item.s3}", color = Color.White)
        }
        if(item.p1?.length!!>0){
            Text(text = "Periodo: ${item.p1}", color = Color.White)
        }else if(item.p2?.length!!>0){
            Text(text = "Periodo: ${item.p2}", color = Color.White)
        }
        else if(item.p3?.length!!>0){
            Text(text = "Periodo: ${item.p3}", color = Color.White)
        }
        if(item.a1?.length!!>0){
            Text(text = "Año: ${item.a1}", color = Color.White)
        }else if(item.a2?.length!! >0){
            Text(text = "Año: ${item.a2}", color = Color.White)
        }
        else if(item.a3?.length!!>0){
            Text(text = "Año: ${item.a3}", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}