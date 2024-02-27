package com.example.login_sicenet.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.login_sicenet.R
import com.example.login_sicenet.data.RetrofitClient
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AllCalifFinalByAlumnosResponse
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionUnidad
import com.example.login_sicenet.model.CargaAcademica
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.EnvelopeCalif
import com.example.login_sicenet.model.EnvelopeCalifUnidad
import com.example.login_sicenet.model.EnvelopeCargaAcademica
import com.example.login_sicenet.model.EnvelopeKardex
import com.example.login_sicenet.model.EnvelopeLogin
import com.example.login_sicenet.model.Kardex
import com.example.login_sicenet.network.AddCookiesInterceptor
import com.example.login_sicenet.screens.SiceUiState.Success
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.simpleframework.xml.core.Persister


@Composable
fun LoginScreen(navController: NavController, viewModel: DataViewModel){
    val context = LocalContext.current
    var user by remember { mutableStateOf("") }
    var pass by remember {
        mutableStateOf("")
    }
    var isValidUser by remember {
        mutableStateOf(false)
    }
    var isValidPass by remember {
        mutableStateOf(false)
    }
    var passVisible by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFE0E0E0)) // Imagen de fondo
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroundsice),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Card(
                Modifier.padding(12.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    RowImage(navController = navController)
                    RowUser(
                        user = user,
                        userChange = {
                            user = it
                            isValidUser = it.length >= 9 //9
                        },
                        isValidUser = isValidUser
                    )
                    RowPass(
                        pass = pass,
                        passChange = {
                            pass = it
                            isValidPass = pass.length >= 0 //6
                        },
                        passVisible = passVisible,
                        passVisibleChange = {
                            passVisible = !passVisible
                        },
                        isValidPass = isValidPass
                    )
                    RowButtonLogin(
                        context = context,
                        isValidUser = isValidUser,
                        isValidPass = isValidPass,
                        nControl = user,
                        password = pass,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RowImage(navController: NavController){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navController.navigate("about_screen")
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoitsur_removebg_preview),
                contentDescription = "Imagen de Login",
                modifier = Modifier.size(100.dp)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowPass(
    pass: String,
    passChange: (String) -> Unit,
    passVisible: Boolean,
    passVisibleChange : () -> Unit,
    isValidPass : Boolean
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = pass,
            onValueChange = passChange,
            maxLines = 1,
            singleLine = true,
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                IconButton(onClick = { passVisibleChange() }){
                    Icon(imageVector = image, contentDescription = "Ver contraseña")
                }
            },
            visualTransformation = if (passVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (isValidPass) Color.Green else Color.Red,
//                unfocusedIndicatorColor = if (isValidPass) Color.Green else Color.Red
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowUser(
    user: String,
    userChange: (String) -> Unit,
    isValidUser : Boolean
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(value = user, onValueChange = userChange,
            label = { Text("Usuario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            colors = if (isValidUser) {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedBorderColor = Color.Green
                )
            } else {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Red,
                    focusedBorderColor = Color.Red
                )
            }
        )
    }
}

@Composable
fun RowButtonLogin(
    context: Context,
    isValidUser: Boolean,
    isValidPass: Boolean,
    nControl: String,
    password: String,
    navController: NavController,
    viewModel: DataViewModel
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                    login(context)
                    viewModel.nControl=nControl
                    viewModel.pass=password
                    viewModel.performLoginAndFetchAcademicProfile()
                    Log.e("ESTATUS",viewModel.siceUiState.toString())
//                    if(viewModel.siceUiState == Success){
//                        //viewModel.getAcademicProfile()
                        if(viewModel.accesoLoginResult?.acceso==true) {
                            navController.navigate("data")
                        }else{
                            showError(context, "ACCESO DENEGADO")
                        }
//                   }
                    //authenticate(context, nControl, password, navController, viewModel)
            },
            enabled = isValidUser) {
            Text("Iniciar Sesión")
        }
    }
}

//CREACION DE REQEUSTS AL SERVIDOR
private fun authenticate(context: Context, matricula: String, contrasenia: String, navController: NavController , viewModel: DataViewModel) {
    val bodyLogin = loginRequestBody(matricula, contrasenia)
    val service = RetrofitClient(context).retrofitService
    service.login(bodyLogin).enqueue(object : Callback<EnvelopeLogin>{
        override fun onResponse(call: Call<EnvelopeLogin>, response: Response<EnvelopeLogin>){
            if (response.isSuccessful) {
                val envelope = response.body()
                val accesoResultJson: String? = envelope?.bodyLogin?.accesoLoginResponse?.accesoLoginResult

                // Deserializa la cadena JSON a AlumnoAcademicoResult
                val json = Json { ignoreUnknownKeys = true }
                val accesoResult: AccesoLoginResult? = accesoResultJson?.let { json.decodeFromString(it) }

                Log.w("Exito", "Se obtuvo el perfil 2: ${accesoResult}")
                val alumnoAcademicoResultJson = Json.encodeToString(accesoResult)

                //VERIFICAR SI LAS CREDENCIALES INGRESADAS SON CORRECTAS
                if(accesoResult?.acceso==true){
                    Log.w("Exito", "Se obtuvo el perfil")
                    viewModel.accesoLoginResult=accesoResult
                    getAcademicProfile(context, navController,viewModel)
                    navController.navigate("data")
                }else{
                    showError(context, "ACCESO DENEGADO")
                }
            } else {
                showError(context, "Error en la autenticación. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeLogin>, t: Throwable){
            t.printStackTrace()
            showError(context, "Error en la solicitud")
        }
    })
}

private fun getAcademicProfile(context: Context, navController: NavController, viewModel: DataViewModel) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = profileRequestBody()
    service.getAcademicProfile(bodyProfile).enqueue(object : Callback<Envelope> {
        override fun onResponse(call: Call<Envelope>, response: Response<Envelope>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                val alumnoResultJson: String? = envelope?.body?.getAlumnoAcademicoWithLineamientoResponse?.getAlumnoAcademicoWithLineamientoResult

                // Deserializa la cadena JSON
                val json = Json { ignoreUnknownKeys = true }
                val alumnoAcademicoResult: AlumnoAcademicoResult? = alumnoResultJson?.let { json.decodeFromString(it) }

                Log.w("Exito", "Se obtuvo el perfil 2: ${alumnoAcademicoResult}")
                val alumnoAcademicoResultJson = Json.encodeToString(alumnoAcademicoResult)

                //BORRAR COOKIES DE SESION DESPUES DE UTILIZARLAS
//                val addCookiesInterceptor = AddCookiesInterceptor(context)
//                addCookiesInterceptor.clearCookies()

                //ALMACENAR Y MOSTRAR LA INFORMACION DEL ALUMNO
                viewModel.alumnoAcademicoResult=alumnoAcademicoResult
//                navController.navigate("data")
                val lineamiento = alumnoAcademicoResult?.lineamiento?.toString() ?: ""
                getCaliFinalProfile(context, navController, viewModel, lineamiento)
            } else {
                showError(context, "Error al obtener el perfil académico. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<Envelope>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

private fun getCaliFinalProfile(context: Context, navController: NavController, viewModel: DataViewModel, lineamiento: String) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = calFinalRequestBody(lineamiento)
    service.getCaliFinales(bodyProfile).enqueue(object : Callback<EnvelopeCalif> {
        override fun onResponse(call: Call<EnvelopeCalif>, response: Response<EnvelopeCalif>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                if (envelope != null) {
                    val alumnoResultJson: String? = envelope.bodyCaliFinal?.allCalifFinalByAlumnosResponse?.getAllCalifFinalByAlumnosResult

                    // Deserializa la cadena JSON
                    if (alumnoResultJson != null) {
                        val json = Json { ignoreUnknownKeys = true }
                        val calificaciones: List<Calificacion> = json.decodeFromString(alumnoResultJson)
                        // Ahora puedes trabajar con la lista de calificaciones
                        // Imprime las calificaciones en el log
                        for (calificacion in calificaciones) {
                            Log.d("Calificaciones", "Calificación: ${calificacion.calif}, Acreditación: ${calificacion.acred}, Grupo: ${calificacion.grupo}, Materia: ${calificacion.materia}, Observaciones: ${calificacion.observaciones}")
                        }
                        viewModel.califFinales=calificaciones
                        getCaliUnidadProfile(context, navController, viewModel, lineamiento)
                        //navController.navigate("data")
                    }
                } else {
                    showError(context, "La respuesta es nula. No se pudieron obtener las calificaciones.")
                }
            } else {
                showError(context, "Error al obtener las calificaciones finales. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeCalif>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

private fun getCaliUnidadProfile(context: Context, navController: NavController, viewModel: DataViewModel, lineamiento: String) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = calUnidadRequestBody()
    service.getCaliUnidades(bodyProfile).enqueue(object : Callback<EnvelopeCalifUnidad> {
        override fun onResponse(call: Call<EnvelopeCalifUnidad>, response: Response<EnvelopeCalifUnidad>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                if (envelope != null) {
                    val alumnoResultJson: String? = envelope.bodyCaliUnidad?.getCalifUnidadesByAlumnoResponse?.getCalifUnidadesByAlumnoResult

                    // Deserializa la cadena JSON
                    if (alumnoResultJson != null) {
                        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
                        val calificaciones: List<CalificacionUnidad> = json.decodeFromString(alumnoResultJson)
                        // Imprime las calificaciones en el log
                        for (calificacion in calificaciones) {
                            Log.d("Calificaciones", "Grupo: ${calificacion.grupo}, Materia: ${calificacion.materia}, Observaciones: ${calificacion.observaciones}, C1: ${calificacion.c1}, C2: ${calificacion.c2}, C3: ${calificacion.c3}, C4: ${calificacion.c4}, C5: ${calificacion.c5}, C6: ${calificacion.c6}, C7: ${calificacion.c7}, C8: ${calificacion.c8}, C9: ${calificacion.c9}, C10: ${calificacion.c10}, C11: ${calificacion.c11}, C12: ${calificacion.c12}, C13: ${calificacion.c13}, Unidades Activas: ${calificacion.unidadesActivas}")
                        }
                        viewModel.calfUnidades=calificaciones
                        getKardexProfile(context, navController, viewModel, lineamiento)
                        //navController.navigate("data")
                    }
                } else {
                    showError(context, "La respuesta es nula. No se pudieron obtener las calificaciones.")
                }
            } else {
                showError(context, "Error al obtener las calificaciones por unidad. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeCalifUnidad>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

private fun getKardexProfile(context: Context, navController: NavController, viewModel: DataViewModel, lineamiento: String) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = kardexRequestBody(lineamiento)
    service.getKardex(bodyProfile).enqueue(object : Callback<EnvelopeKardex> {
        override fun onResponse(call: Call<EnvelopeKardex>, response: Response<EnvelopeKardex>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                if (envelope != null) {
                    val alumnoResultJson: String? = envelope.bodyKardex?.getAllKardexConPromedioByAlumnoResponse?.getAllKardexConPromedioByAlumnoResult
                    // Deserializa la cadena JSON
                    if (alumnoResultJson != null) {
                        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
                        val kardex: Kardex = json.decodeFromString(alumnoResultJson)
                        // Imprime las calificaciones en el log
                        for (kardexItem in kardex.lstKardex) {
                            Log.d("Kardex", "Clave Materia: ${kardexItem.clvMat}, Clave Oficial Materia: ${kardexItem.clvOfiMat}, Materia: ${kardexItem.materia}, Créditos: ${kardexItem.cdts}, Calificación: ${kardexItem.calif}, Acreditación: ${kardexItem.acred}, Semestre 1: ${kardexItem.s1}, Periodo 1: ${kardexItem.p1}, Año 1: ${kardexItem.a1}, Semestre 2: ${kardexItem.s2}, Periodo 2: ${kardexItem.p2}, Año 2: ${kardexItem.a2}")
                        }

                        val promedio = kardex.promedio
                        Log.d("Promedio", "Promedio General: ${promedio.promedioGral}, Créditos Acumulados: ${promedio.cdtsAcum}, Créditos Plan: ${promedio.cdtsPlan}, Materias Cursadas: ${promedio.matCursadas}, Materias Aprobadas: ${promedio.matAprobadas}, Avance Créditos: ${promedio.avanceCdts}")
                        viewModel.kardex=kardex
                        getCargaAcadProfile(context, navController, viewModel)
                        //navController.navigate("data")
                    } else {
                        showError(context, "La respuesta es nula. No se pudo obtener el kardex.")
                    }
                } else {
                    showError(context, "La respuesta del servidor es nula. No se pudo obtener el kardex.")
                }
            } else {
                showError(context, "Error al obtener el kardex. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeKardex>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

private fun getCargaAcadProfile(context: Context, navController: NavController, viewModel: DataViewModel) {
    val service = RetrofitClient(context).retrofitService
    val bodyProfile = cargaAcademicaRequestBody()
    service.getCargaAcademica(bodyProfile).enqueue(object : Callback<EnvelopeCargaAcademica> {
        override fun onResponse(call: Call<EnvelopeCargaAcademica>, response: Response<EnvelopeCargaAcademica>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                if (envelope != null) {
                    val alumnoResultJson: String? = envelope.bodyCargaAcademica?.getCargaAcademicaByAlumnoResponse?.getCargaAcademicaByAlumnoResult                    // Deserializa la cadena JSON
                    if (alumnoResultJson != null) {
                        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
                        val cargaAcademica: List<CargaAcademicaItem> = json.decodeFromString(alumnoResultJson)
                        // Imprime la carga académica en el log
                        for (cargaAcademicaItem in cargaAcademica) {
                            Log.d("Carga Académica", "Docente: ${cargaAcademicaItem.docente}, Clave Oficial: ${cargaAcademicaItem.clvOficial}, Materia: ${cargaAcademicaItem.materia}, Créditos Materia: ${cargaAcademicaItem.creditosMateria}, Grupo: ${cargaAcademicaItem.grupo}")
                        }
                        viewModel.cargaAcademica=cargaAcademica
                        navController.navigate("data")
                        //BORRAR COOKIES DE SESION DESPUES DE UTILIZARLAS
                        val addCookiesInterceptor = AddCookiesInterceptor(context)
                        addCookiesInterceptor.clearCookies()
                    } else {
                        showError(context, "La respuesta es nula. No se pudo obtener la carga académica.")
                    }
                } else {
                    showError(context, "La respuesta del servidor es nula. No se pudo obtener la carga académica.")
                }
            } else {
                showError(context, "Error al obtener la carga académica. Código de respuesta: ${response.code()}")
            }
        }
        override fun onFailure(call: Call<EnvelopeCargaAcademica>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del la carga")
        }
    })
}



//MENSAJES FLOTANTES
fun login(context: Context){
    Toast.makeText(context, "Iniciando sesión", Toast.LENGTH_SHORT).show()
}

private fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//BODYS DE PETICION
fun loginRequestBody(matricula: String, contrasenia: String): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <accesoLogin xmlns="http://tempuri.org/">
              <strMatricula>$matricula</strMatricula>
              <strContrasenia>$contrasenia</strContrasenia>
              <tipoUsuario>ALUMNO</tipoUsuario>
            </accesoLogin>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun profileRequestBody(): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" />
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun calFinalRequestBody(lineamiento: String): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAllCalifFinalByAlumnos xmlns="http://tempuri.org/">
              <bytModEducativo>$lineamiento</bytModEducativo>
            </getAllCalifFinalByAlumnos>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun calUnidadRequestBody(): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun kardexRequestBody(lineamiento: String): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getAllKardexConPromedioByAlumno xmlns="http://tempuri.org/">
              <aluLineamiento>$lineamiento</aluLineamiento>
            </getAllKardexConPromedioByAlumno>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}

private fun cargaAcademicaRequestBody(): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <getCargaAcademicaByAlumno xmlns="http://tempuri.org/" />
          </soap:Body>
        </soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}