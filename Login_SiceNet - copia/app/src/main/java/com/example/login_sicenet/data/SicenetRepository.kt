package com.example.login_sicenet.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.login_sicenet.model.AccesoLoginResponse
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.BodyLogin
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionUnidad
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.Envelope
import com.example.login_sicenet.model.EnvelopeCalif
import com.example.login_sicenet.model.EnvelopeCalifUnidad
import com.example.login_sicenet.model.EnvelopeCargaAcademica
import com.example.login_sicenet.model.EnvelopeKardex
import com.example.login_sicenet.model.EnvelopeLogin
import com.example.login_sicenet.model.Kardex
import com.example.login_sicenet.model.KardexItem
import com.example.login_sicenet.network.LoginSICEApiService
import com.example.login_sicenet.screens.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

interface SicenetRepository {
    suspend fun login(matricula: String, contrasenia: String): AccesoLoginResult
    suspend fun getAcademicProfile(): AlumnoAcademicoResult
    suspend fun getCaliFinales(lineamiento: String): List<Calificacion>
    suspend fun getCaliUnidades(): List<CalificacionUnidad>
    suspend fun getKardex(lineamiento: String): Kardex
    suspend fun getCargaAcademica(): List<CargaAcademicaItem>
}

class NetworkSicenetRepository(
    private val loginSICEApiService: LoginSICEApiService
) : SicenetRepository {
    override suspend fun login(matricula: String, contrasenia: String): AccesoLoginResult {
        //val service = RetrofitClient(context).retrofitService
        val response = loginSICEApiService.login(loginRequestBody(matricula,contrasenia)).execute()

        if (response.isSuccessful) {
            val envelopeLogin = response.body()
            val accesoResultJson: String? = envelopeLogin?.bodyLogin?.accesoLoginResponse?.accesoLoginResult

            val json = Json { ignoreUnknownKeys = true }
            return accesoResultJson?.let { json.decodeFromString(it) } ?: AccesoLoginResult()
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }


    override suspend fun getAcademicProfile(): AlumnoAcademicoResult{
        val response = loginSICEApiService.getAcademicProfile(profileRequestBody()).execute()

        if (response.isSuccessful) {
            val envelopeProfile = response.body()
            val alumnoResultJson: String? = envelopeProfile?.body?.getAlumnoAcademicoWithLineamientoResponse?.getAlumnoAcademicoWithLineamientoResult

            val json = Json { ignoreUnknownKeys = true }
            return alumnoResultJson?.let { json.decodeFromString(it) } ?: AlumnoAcademicoResult()
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }
    override suspend fun getCaliFinales(lineamiento: String): List<Calificacion> {
        val response = loginSICEApiService.getCaliFinales(calFinalRequestBody(lineamiento)).execute()

        if (response.isSuccessful) {
            val envelopeCaliFinal = response.body()
            val alumnoResultJson: String? = envelopeCaliFinal?.bodyCaliFinal?.allCalifFinalByAlumnosResponse?.getAllCalifFinalByAlumnosResult

            val json = Json { ignoreUnknownKeys = true }
            val calificaciones: List<Calificacion> = json.decodeFromString(alumnoResultJson ?: "")
            return calificaciones
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }
    override suspend fun getCaliUnidades(): List<CalificacionUnidad> {
        val response = loginSICEApiService.getCaliUnidades(calUnidadRequestBody()).execute()

        if (response.isSuccessful) {
            val envelopeCaliUnidad = response.body()
            val alumnoResultJson: String? = envelopeCaliUnidad?.bodyCaliUnidad?.getCalifUnidadesByAlumnoResponse?.getCalifUnidadesByAlumnoResult

            val json = Json { ignoreUnknownKeys = true }
            val calificaciones: List<CalificacionUnidad> = json.decodeFromString(alumnoResultJson ?: "")
            return calificaciones
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }
    override suspend fun getKardex(lineamiento: String): Kardex {
        val response = loginSICEApiService.getKardex(kardexRequestBody(lineamiento)).execute()

        if (response.isSuccessful) {
            val envelopeKardex = response.body()
            val alumnoResultJson: String? = envelopeKardex?.bodyKardex?.getAllKardexConPromedioByAlumnoResponse?.getAllKardexConPromedioByAlumnoResult

            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
            val kardex: Kardex = json.decodeFromString(alumnoResultJson?: "")
            Log.e("kardex",kardex.promedio.promedioGral.toString())
            return kardex
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }
    override suspend fun getCargaAcademica(): List<CargaAcademicaItem> {
        val response = loginSICEApiService.getCargaAcademica(cargaAcademicaRequestBody()).execute()

        if (response.isSuccessful) {
            val envelope = response.body()
            val alumnoResultJson: String? = envelope?.bodyCargaAcademica?.getCargaAcademicaByAlumnoResponse?.getCargaAcademicaByAlumnoResult                    // Deserializa la cadena JSON

            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
            val cargaAcademica: List<CargaAcademicaItem> = json.decodeFromString(alumnoResultJson?:"")
            return cargaAcademica
        } else {
            // Manejar el error en caso de respuesta no exitosa
            throw IOException("Error en la autenticación. Código de respuesta: ${response.code()}")
        }
    }
}

//BODYS DE PETICION
private fun loginRequestBody(matricula: String, contrasenia: String): RequestBody {
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