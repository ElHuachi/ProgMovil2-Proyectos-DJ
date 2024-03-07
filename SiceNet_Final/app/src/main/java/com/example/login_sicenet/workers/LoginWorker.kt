package com.example.login_sicenet.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.data.DBRepositories.AccessLoginResponseRepository
import com.example.login_sicenet.data.DBRepositories.AlumnoAcademicoWithLineamientoRepository
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.model.AccesoDetails
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AccesoUiState
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.ProfileDetails
import com.example.login_sicenet.model.ProfileUiState
import com.example.login_sicenet.model.toItem
import com.example.login_sicenet.model.toItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val sicenetRepository = (applicationContext as SicenetApplication).container.SicenetRepository
        val accessRepository = (applicationContext as SicenetApplication).container.AccessLoginResponseRepository
        val profileRepository = (applicationContext as SicenetApplication).container.AlumnoAcademicoWithLineamientoRepository
        val accesoDetails = AccesoDetails()
        val profileDetails = ProfileDetails()
        try{
            val matricula = inputData.getString("matricula")
            val pass = inputData.getString("password")

            //OBTENER PERFIL
            val loginResult = sicenetRepository.login(matricula?:"",pass?:"")

            val academicProfile = sicenetRepository.getAcademicProfile()

            Log.d("LoginWorker","exito")

            //ALMACENAR EN LA BASE DE DATOS

            @RequiresApi(Build.VERSION_CODES.O)
            fun updateUiStateAccess(accessDetails: AccesoDetails, AccessLoginResult: AccesoLoginResult): AccesoUiState {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "COMPRATE OTRO PHONE"
                }
                val updatedAccesoDetails = accessDetails.copy(
                    acceso = AccessLoginResult.acceso,
                    matricula = AccessLoginResult.matricula,
                    estatus = AccessLoginResult.estatus,
                    tipo_usuario = AccessLoginResult.tipoUsuario,
                    contrasenia = AccessLoginResult.contrasenia,
                    fecha = currentDateTime
                )
                return AccesoUiState(accesoDetails = updatedAccesoDetails, isEntryValid = true)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun updateUiStateProfile(profileDetails: ProfileDetails, AlumnoAcademicoResult: AlumnoAcademicoResult): ProfileUiState {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "COMPRATE OTRO PHONE"
                }
                val updatedProfileDetails = profileDetails.copy(matricula = AlumnoAcademicoResult.matricula, fechaReins = AlumnoAcademicoResult.fechaReins,
                    estatus =  AlumnoAcademicoResult.estatus, modEducativo = AlumnoAcademicoResult.modEducativo, adeudo = AlumnoAcademicoResult.adeudo,
                    urlFoto = AlumnoAcademicoResult.urlFoto, adeudoDescripcion = AlumnoAcademicoResult.adeudoDescripcion,
                    inscrito = AlumnoAcademicoResult.inscrito, semActual = AlumnoAcademicoResult.semActual, cdtosActuales = AlumnoAcademicoResult.cdtosActuales,
                    cdtosAcumulados = AlumnoAcademicoResult.cdtosAcumulados, especialidad = AlumnoAcademicoResult.especialidad,
                    carrera = AlumnoAcademicoResult.carrera, lineamiento = AlumnoAcademicoResult.lineamiento, nombre = AlumnoAcademicoResult.nombre,
                    fecha = currentDateTime
                )
                return ProfileUiState(profileDetails = updatedProfileDetails, isEntryValid = true)
            }

            suspend fun getAccesoExistente(matricula: String): Boolean {
                return try {
                    // Utiliza withContext para cambiar al hilo adecuado
                    withContext(Dispatchers.IO) {
                        val accesoResponse = accessRepository.getItemStream(matricula)
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
            suspend fun updateProfileDB() {
                var fecha  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                profileRepository.updateItemQuery(matricula?:"",fecha)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            suspend fun updateAccessDB() {
                var fecha  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } else {
                    "hoy"
                }
                accessRepository.updateItemQuery(matricula?:"",fecha)
            }

            // Realiza las inserciones en la base de datos

            if(getAccesoExistente(matricula?:"")){
                updateAccessDB()
                updateProfileDB()
            }else{
                val accessId = accessRepository.insertItemAndGetId(updateUiStateAccess(accesoDetails,loginResult).accesoDetails.toItem())
                val profileId = profileRepository.insertItemAndGetId(updateUiStateProfile(profileDetails,academicProfile).profileDetails.toItem())
            }

            return Result.success()
        }catch (e: Exception){
            Log.e("error",e.message?:"")
            return Result.failure()
        }
    }

}