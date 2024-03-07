package com.example.login_sicenet.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.toItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CargaAcademicaWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val sicenetRepository = (applicationContext as SicenetApplication).container.SicenetRepository
        val cargaAcRepository = (applicationContext as SicenetApplication).container.CargaAcademicaRepository
        try{
            //OBTENER CARGA ACADEMICA
            val matricula = inputData.getString("matricula")
            val cargaAcademica = sicenetRepository.getCargaAcademica()

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun saveCargaAc() {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                for(clase in cargaAcademica!!){
                    val clase = CargaAcademicaItemDB(
                        matricula = matricula,
                        observaciones = clase.observaciones,
                        semipresencial = clase.semipresencial,
                        docente = clase.docente,
                        clvOficial = clase.clvOficial,
                        sabado = clase.sabado,
                        viernes = clase.viernes,
                        jueves = clase.jueves,
                        miercoles = clase.miercoles,
                        martes = clase.martes,
                        lunes = clase.lunes,
                        materia = clase.materia,
                        estadoMateria = clase.estadoMateria,
                        creditosMateria = clase.creditosMateria,
                        grupo = clase.grupo,
                        fecha= currentDateTime
                    )
                    val califId = cargaAcRepository.insertItemAndGetId(clase)
                }
            }

            suspend fun getCargaAcExistente(matricula: String): Boolean {
                return try {
                    // Utiliza withContext para cambiar al hilo adecuado
                    withContext(Dispatchers.IO) {
                        val accesoResponse = cargaAcRepository.getItemStream(matricula)
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
            suspend fun updateCargaAc() {
                var fecha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                cargaAcRepository.updateQuery(matricula ?: "", fecha)
            }

            if (getCargaAcExistente(matricula ?: "")) {
                updateCargaAc()
            } else {
                saveCargaAc()
            }

            return Result.success()
        }catch (e: Exception){
            Log.e("error",e.message?:"")
            return Result.failure()
        }

    }
}