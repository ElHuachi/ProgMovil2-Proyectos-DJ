package com.example.login_sicenet.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.model.CalifUnidadDetails
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.toItem
import com.example.login_sicenet.model.toItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalifUnidadWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
    val sicenetRepository = (applicationContext as SicenetApplication).container.SicenetRepository
    val califUnidadRepository = (applicationContext as SicenetApplication).container.CaliPorUnidadRepository
    val califUnidadDetails = CalifUnidadDetails()
    try{
        //OBTENER CALIFICACIONES
        val matricula = inputData.getString("matricula")
        val califUnidad = sicenetRepository.getCaliUnidades()

        //ALMACENAR EN LA BASE DE DATOS

        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun saveCaliUnidad() {
            val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } else {
                "hoy"
            }
            for(cali in califUnidad!!){
                val calif = CalificacionUnidadDB(
                    matricula = matricula,
                    observaciones = cali.observaciones,
                    c13 = cali.c13,
                    c12 = cali.c12,
                    c11 = cali.c11,
                    c10 = cali.c10,
                    c9 = cali.c9,
                    c8 = cali.c8,
                    c7 = cali.c7,
                    c6 = cali.c6,
                    c5 = cali.c5,
                    c4 = cali.c4,
                    c3 = cali.c3,
                    c2 = cali.c2,
                    c1 = cali.c1,
                    unidadesActivas = cali.unidadesActivas,
                    materia = cali.materia,
                    grupo = cali.grupo,
                    fecha= currentDateTime
                )
                val califId = califUnidadRepository.insertItemAndGetId(calif)
            }

        }

        suspend fun getCalifUExistente(matricula: String): Boolean {
            return try {
                // Utiliza withContext para cambiar al hilo adecuado
                withContext(Dispatchers.IO) {
                    val accesoResponse = califUnidadRepository.getItemStream(matricula)
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
        suspend fun updateCalifU() {
            var fecha  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } else {
                "hoy"
            }
            califUnidadRepository.updateQuery(matricula?:"",fecha)
        }

        if(getCalifUExistente(matricula?:"")){
            updateCalifU()
        }else{
            saveCaliUnidad()
        }

        return Result.success()
    }catch (e: Exception){
        Log.e("error",e.message?:"")
        return Result.failure()
    }
    }

}