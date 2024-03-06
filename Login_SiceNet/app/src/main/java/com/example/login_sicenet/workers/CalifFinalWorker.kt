package com.example.login_sicenet.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.model.CalifFinDetails
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.toItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalifFinalWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val sicenetRepository =
            (applicationContext as SicenetApplication).container.SicenetRepository
        val califFinalRepository =
            (applicationContext as SicenetApplication).container.CaliFinalesRepository
        val califFinalDetails = CalifFinDetails()
        try {
            //OBTENER CALIFICACIONES
            val matricula = inputData.getString("matricula")
            val califFinales = sicenetRepository.getCaliFinales("3")

            //ALMACENAR EN LA BASE DE DATOS
            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun saveCaliFinal() {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                for (cali in califFinales!!) {
                    val calif = CalificacionDB(
                        matricula = matricula,
                        observaciones = cali.observaciones,
                        acred = cali.acred,
                        calif = cali.calif,
                        materia = cali.materia,
                        grupo = cali.grupo,
                        fecha = currentDateTime
                    )
                    val califId = califFinalRepository.insertItemAndGetId(calif)
                }
            }

            suspend fun getCalifFinalExistente(matricula: String): Boolean {
                return try {
                    // Utiliza withContext para cambiar al hilo adecuado
                    withContext(Dispatchers.IO) {
                        val accesoResponse = califFinalRepository.getItemStream(matricula)
                            .firstOrNull()
                            ?.toItemUiState(true)

                        // Devuelve true si el acceso existe, de lo contrario false
                        accesoResponse != null
                    }
                } catch (e: Exception) {
                    // Manejar la excepción, puedes logearla o manejarla de acuerdo a tus necesidades
                    false
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun updateCalifFinal() {
                var fecha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                califFinalRepository.updateQuery(matricula ?: "", fecha)
            }

            if (getCalifFinalExistente(matricula ?: "")) {
                updateCalifFinal()
            } else {
                saveCaliFinal()
            }

            return Result.success()
        } catch (e: Exception) {
            Log.e("error", e.message ?: "")
            return Result.failure()
        }

    }
}