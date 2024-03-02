package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login_sicenet.R

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
    ) {
        BodyContent(navController, viewModel)
    }
}