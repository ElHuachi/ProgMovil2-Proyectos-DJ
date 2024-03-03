package com.example.login_sicenet.screens

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.util.Log
import android.webkit.CookieManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.login_sicenet.R
import com.example.login_sicenet.data.AppContainer
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.navigation.AppScreens
import com.example.login_sicenet.network.AddCookiesInterceptor
import com.example.login_sicenet.ui.theme.Green80
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun DataScreen(navController: NavController, viewModel: DataViewModel) {
    var expanded by remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        IconButton(onClick = { navController.navigate("data") }) {
                            Icon(imageVector = Icons.Filled.House, contentDescription = "Inicio"
                                )

                        }
                        Text(
                            text = "Perfil Académico",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .padding(horizontal = 70.dp)
                        )
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(imageVector = Icons.Filled.DensityMedium, contentDescription = "Más Opciones",
                                modifier = Modifier
                                    .size(40.dp))
//                                Icon(
//                                    painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
//                                    contentDescription = "SiceNet Logo",
//                                    modifier = Modifier
//                                        .size(40.dp)
//                                )
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(text = { Text(text = "Información del alumno") }, onClick = { navController.navigate("data") })
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
            BodyContent(navController, viewModel)

        }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BodyContent(navController: NavController, viewModel: DataViewModel) {
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
                val alumnoAcademicoResult = viewModel.alumnoAcademicoResult
                // Verifica si alumnoAcademicoResult es null
                if (alumnoAcademicoResult != null) {

                    val cardColors = CardDefaults.cardColors(
                        containerColor = Color(0xFF00BFA5),
                        contentColor = Color.Black,

                        )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Nombre",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.nombre}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Matrícula",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.matricula}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Carrera",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.carrera}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Especialidad",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.especialidad}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Semestre actual",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.semActual}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Créditos acumulados",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.cdtosAcumulados}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Créditos actuales",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    Text(
                                        text = "${alumnoAcademicoResult.cdtosActuales}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener el perfil académico.")
                    }
                }
            }else{
                Log.d("obteniendo perfil", "obteniendo perfil")
                coroutineScope.launch {
                    if(viewModel.getAccesoExistente(viewModel.nControl)==true){
                        viewModel.perfilDB=viewModel.getProfileDB(viewModel.nControl)
                    }
                    //viewModel.deleteAccessDB("S20120179")
                }
                val perfilDB : AlumnoAcademicoResultDB? = viewModel.perfilDB
                //PANTALLA LLENADA DESDE LA BASE DE DATOS
                if (perfilDB != null) {
                    val cardColors = CardDefaults.cardColors(
                        containerColor = Color(0xFF00BFA5),
                        contentColor = Color.Black,

                        )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Nombre",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.nombre}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Matrícula",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.matricula}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Carrera",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.carrera}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Especialidad",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.especialidad}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Semestre actual",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.semActual}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Créditos acumulados",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.cdtosAcumulados}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = cardColors
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .sizeIn(minHeight = 40.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Créditos actuales",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.background(color = Color(0x00000000))
                                    )
                                    if (perfilDB != null) {
                                        Text(
                                            text = "${perfilDB?.cdtosActuales}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Última actulizacíon: ${perfilDB?.fecha}", color = Color.White)
                        }
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "No se pudo obtener el perfil académico.", color = Color.White)
                    }
                }
            }

            Button(onClick = {
                // Navegar a la pantalla de login
                val addCookiesInterceptor = AddCookiesInterceptor(context)
                addCookiesInterceptor.clearCookies()
                viewModel.accesoLoginResult?.acceso=false
                viewModel.perfilDB = null
                navController.popBackStack(route = "login", inclusive = false)
            }) {
                Text(text = "Cerrar sesion")
            }
        }
    }
}

