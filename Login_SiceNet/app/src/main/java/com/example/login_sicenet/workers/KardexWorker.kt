package com.example.login_sicenet.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.PromedioDB
import com.example.login_sicenet.model.toItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class KardexWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val sicenetRepository =
            (applicationContext as SicenetApplication).container.SicenetRepository
        val kardexRepository =
            (applicationContext as SicenetApplication).container.KardexItemRepository
        val promedioRepository =
            (applicationContext as SicenetApplication).container.PromedioRepository
        try {
            //OBTENER CARGA ACADEMICA
            val matricula = inputData.getString("matricula")
            val kardex = sicenetRepository.getKardex("3")

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun saveKardex() {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                for (item in kardex?.lstKardex!!) {
                    val item = KardexItemDB(
                        matricula = matricula,
                        s3 = item.s3,
                        p3 = item.p3,
                        a3 = item.a3,
                        s2 = item.s2,
                        p2 = item.p2,
                        a2 = item.a2,
                        s1 = item.s1,
                        p1 = item.p1,
                        a1 = item.a1,
                        clvMat = item.clvMat,
                        clvOfiMat = item.clvOfiMat,
                        cdts = item.cdts,
                        calif = item.calif,
                        materia = item.materia,
                        acred = item.acred,
                        fecha = currentDateTime
                    )
                    val califId = kardexRepository.insertItem(item)
                }
            }

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun savePromedio() {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                val item = PromedioDB(
                    matricula = matricula,
                    promedioGral = kardex?.promedio?.promedioGral,
                    cdtsAcum = kardex?.promedio?.cdtsAcum,
                    cdtsPlan = kardex?.promedio?.cdtsPlan,
                    matCursadas = kardex?.promedio?.matCursadas,
                    matAprobadas = kardex?.promedio?.matAprobadas,
                    avanceCdts = kardex?.promedio?.avanceCdts,
                    fecha = currentDateTime
                )
                val califId = promedioRepository.insertItemAndGetId(item)
            }

            suspend fun getKardexExistente(matricula: String): Boolean {
                return try {
                    // Utiliza withContext para cambiar al hilo adecuado
                    withContext(Dispatchers.IO) {
                        val accesoResponse = kardexRepository.getItemStream(matricula)
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
            suspend fun updateKardex() {
                var fecha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                kardexRepository.updateQuery(matricula ?: "", fecha)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun updatePromedio() {
                var fecha = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                promedioRepository.updateQuery(matricula ?: "", fecha)
            }

            if (getKardexExistente(matricula ?: "")) {
                updateKardex()
                updatePromedio()
            } else {
                saveKardex()
                savePromedio()
            }

            return Result.success()
        } catch (e: Exception) {
            Log.e("error", e.message ?: "")
            return Result.failure()
        }
    }
}