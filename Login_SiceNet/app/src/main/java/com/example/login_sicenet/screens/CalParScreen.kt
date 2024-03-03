package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
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
                IconButton(onClick = { navController.navigate("data") }) {
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
        // Adjust the padding for the content to prevent it from being hidden by the top app bar
        BodyContentCalif(navController, viewModel, Modifier.padding(top = innerPadding.calculateTopPadding()))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun BodyContentCalif(navController: NavController, viewModel: DataViewModel, Modifier: Modifier) {
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
                        if(existente==true){
                            Log.e("ya estan", "ya estan")

                        }else{
                            viewModel.saveCaliUnidad()

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
                    viewModel.caliUnidadDB1=viewModel.getCaliUnidad1(viewModel.nControl)

                    //viewModel.deleteAccessDB("S20120179")
                }
                val caliDB = viewModel.caliUnidadDB1
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (caliDB != null) {

                    LazyColumn {
                        items(1) { item ->
                            // Function to display each item
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
}

@Composable
fun DisplayItem(item: CalificacionUnidad) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Grupo: ${item.grupo}", color = Color.White)
        if(item.unidadesActivas.length>=1){
            Text(text = "U1: ${item.c1}", color = Color.White)
        }
        if(item.unidadesActivas.length>=2){
            Text(text = "U2: ${item.c2}", color = Color.White)
        }
        if(item.unidadesActivas.length>=3){
            Text(text = "U3: ${item.c3}", color = Color.White)
        }
        if(item.unidadesActivas.length>=4){
            Text(text = "U4: ${item.c4}", color = Color.White)
        }
        if(item.unidadesActivas.length>=5){
            Text(text = "U5: ${item.c5}", color = Color.White)
        }
        if(item.unidadesActivas.length>=6){
            Text(text = "U6: ${item.c6}", color = Color.White)
        }
        if(item.unidadesActivas.length>=7){
            Text(text = "U7: ${item.c7}", color = Color.White)
        }
        if(item.unidadesActivas.length>=8){
            Text(text = "U8: ${item.c8}", color = Color.White)
        }
        if(item.unidadesActivas.length>=9){
            Text(text = "U9: ${item.c9}", color = Color.White)
        }
        if(item.unidadesActivas.length>=10){
            Text(text = "U10: ${item.c10}", color = Color.White)
        }
        if(item.unidadesActivas.length>=11){
            Text(text = "U11: ${item.c11}", color = Color.White)
        }
        if(item.unidadesActivas.length>=12){
            Text(text = "U12: ${item.c12}", color = Color.White)
        }
        if(item.unidadesActivas.length==13){
            Text(text = "U13: ${item.c13}", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DisplayItemDB(item: CalificacionUnidadDB) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display each property of the object in a Text or other Compose components
        Text(text = "Materia: ${item.materia}", color = Color.White)
        Text(text = "Grupo: ${item.grupo}", color = Color.White)
        if(item.unidadesActivas.length>=1){
            Text(text = "U1: ${item.c1}", color = Color.White)
        }
        if(item.unidadesActivas.length>=2){
            Text(text = "U2: ${item.c2}", color = Color.White)
        }
        if(item.unidadesActivas.length>=3){
            Text(text = "U3: ${item.c3}", color = Color.White)
        }
        if(item.unidadesActivas.length>=4){
            Text(text = "U4: ${item.c4}", color = Color.White)
        }
        if(item.unidadesActivas.length>=5){
            Text(text = "U5: ${item.c5}", color = Color.White)
        }
        if(item.unidadesActivas.length>=6){
            Text(text = "U6: ${item.c6}", color = Color.White)
        }
        if(item.unidadesActivas.length>=7){
            Text(text = "U7: ${item.c7}", color = Color.White)
        }
        if(item.unidadesActivas.length>=8){
            Text(text = "U8: ${item.c8}", color = Color.White)
        }
        if(item.unidadesActivas.length>=9){
            Text(text = "U9: ${item.c9}", color = Color.White)
        }
        if(item.unidadesActivas.length>=10){
            Text(text = "U10: ${item.c10}", color = Color.White)
        }
        if(item.unidadesActivas.length>=11){
            Text(text = "U11: ${item.c11}", color = Color.White)
        }
        if(item.unidadesActivas.length>=12){
            Text(text = "U12: ${item.c12}", color = Color.White)
        }
        if(item.unidadesActivas.length==13){
            Text(text = "U13: ${item.c13}", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
