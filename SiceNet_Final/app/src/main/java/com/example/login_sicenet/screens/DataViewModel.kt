package com.example.login_sicenet.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.data.DBRepositories.AccessLoginResponseRepository
import com.example.login_sicenet.data.DBRepositories.AlumnoAcademicoWithLineamientoRepository
import com.example.login_sicenet.data.DBRepositories.CaliFinalesRepository
import com.example.login_sicenet.data.DBRepositories.CaliPorUnidadRepository
import com.example.login_sicenet.data.DBRepositories.CargaAcademicaRepository
import com.example.login_sicenet.data.DBRepositories.KardexItemRepository
import com.example.login_sicenet.data.DBRepositories.PromedioRepository
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.model.AccesoDetails
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AccesoUiState
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CaliFinalesUiState
import com.example.login_sicenet.model.CaliUnidadUiState
import com.example.login_sicenet.model.CalifFinDetails
import com.example.login_sicenet.model.CalifUnidadDetails
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidad
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.CargaAcDetails
import com.example.login_sicenet.model.CargaAcUiState
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.Kardex
import com.example.login_sicenet.model.KardexItem
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.KardexItemDetails
import com.example.login_sicenet.model.KardexUiState
import com.example.login_sicenet.model.ProfileDetails
import com.example.login_sicenet.model.ProfileUiState
import com.example.login_sicenet.model.Promedio
import com.example.login_sicenet.model.PromedioDB
import com.example.login_sicenet.model.PromedioDetails
import com.example.login_sicenet.model.PromedioUiState
import com.example.login_sicenet.model.toItem
import com.example.login_sicenet.model.toItemDetails
import com.example.login_sicenet.model.toItemUiState
import com.example.login_sicenet.workers.CalifFinalWorker
import com.example.login_sicenet.workers.CalifUnidadWorker
import com.example.login_sicenet.workers.CargaAcademicaWorker
import com.example.login_sicenet.workers.KardexWorker
import com.example.login_sicenet.workers.LoginWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface SiceUiState {
    object Success : SiceUiState
    object Error : SiceUiState
    object Loading : SiceUiState
}

class DataViewModel(private val SicenetRepository: SicenetRepository,
                    private val AccessLoginResponseRepository: AccessLoginResponseRepository,
                    private val AlumnoAcademicoWithLineamientoRepository: AlumnoAcademicoWithLineamientoRepository,
                    private val CaliFinalesRepository: CaliFinalesRepository,
                    private val CaliPorUnidadRepository: CaliPorUnidadRepository,
                    private val CargaAcademicaRepository: CargaAcademicaRepository,
                    private val KardexItemRepository: KardexItemRepository,
                    private val PromedioRepository: PromedioRepository,
) : ViewModel() {
    //VARIABLES PARA ALMACENAR LAS RESPUESTAS DEL SERVIDOR
    var accesoLoginResult: AccesoLoginResult? = null
    var alumnoAcademicoResult: AlumnoAcademicoResult? = null
    var califFinales: List<Calificacion>? = null
    var califUnidades: List<CalificacionUnidad>? = null
    var kardex: Kardex? = null
    var cargaAcademica: List<CargaAcademicaItem>? = null

    var nControl: String = ""
    var pass: String = ""
    var lineamiento: String = "2"
    var internet: Boolean = true

    var perfilDB: AlumnoAcademicoResultDB? = null
    var caliUnidadDB: List<CalificacionUnidadDB>? = null
    var caliFinalDB: List<CalificacionDB>? = null
    var cargaAcDB: List<CargaAcademicaItemDB>? = null
    var kardexDB: List<KardexItemDB>? = null
    var promedioDB1: PromedioDB? = null

    //SOLICITUDES AL SERVIDOR
    var siceUiState: SiceUiState by mutableStateOf(SiceUiState.Loading)
        private set

    fun login() {
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.login(nControl, pass)
                }
                accesoLoginResult = result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    fun loginAndGetProfile() {
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading

            try {
                // Realizar el login
                val loginResult = withContext(Dispatchers.IO) {
                    SicenetRepository.login(nControl, pass)
                }

                // Actualizar el estado o manejar el resultado del login según sea necesario
                accesoLoginResult = loginResult

                // Si el login fue exitoso, proceder a obtener el perfil académico
                if (loginResult is AccesoLoginResult) {
                    val academicProfileResult = withContext(Dispatchers.IO) {
                        SicenetRepository.getAcademicProfile()
                    }

                    // Actualizar el estado o manejar el resultado del perfil académico según sea necesario
                    alumnoAcademicoResult = academicProfileResult
                    siceUiState = SiceUiState.Success
                } else {
                    // Manejar el caso en que el login no fue exitoso
                    siceUiState = SiceUiState.Error
                }
            } catch (e: IOException) {
                // Manejar errores de red u otros
                siceUiState = SiceUiState.Error
            } catch (e: HttpException) {
                // Manejar errores HTTP
                siceUiState = SiceUiState.Error
            }
        }
    }

    fun getAcademicProfile(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.getAcademicProfile()
                }
                alumnoAcademicoResult=result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    fun getCalifFinales(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.getCaliFinales(lineamiento)
                }
                califFinales=result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    fun getCalifUnidades(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.getCaliUnidades()
                }
                califUnidades=result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    fun getKardex(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.getKardex(lineamiento)
                }
                kardex=result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    fun getCargaAcademica(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = withContext(Dispatchers.IO) {
                    // Esta llamada se realiza en un hilo de fondo (IO thread)
                    SicenetRepository.getCargaAcademica()
                }
                cargaAcademica=result
                SiceUiState.Success
            } catch (e: IOException) {
                SiceUiState.Error
            } catch (e: HttpException) {
                SiceUiState.Error
            }
        }
    }

    //BASE DE DATOS
    //ACCESO
    suspend fun getAccesoExistente(matricula: String): Boolean? {
        // Utiliza viewModelScope.async para obtener un Deferred
        val deferred = viewModelScope.async {
            AccessLoginResponseRepository.getItemStream(matricula)
                .firstOrNull()
                ?.toItemUiState(true)
        }

        return deferred.await() != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteAccessDB(matricula: String) {
        AccessLoginResponseRepository.deleteItem(matricula)
    }

    //PROFILE
    suspend fun getProfileDB(matricula: String): AlumnoAcademicoResultDB? {
        var profileResponse: ProfileUiState? = null

        // Utiliza viewModelScope.async para obtener un Deferred
        val deferred = viewModelScope.async {
            AlumnoAcademicoWithLineamientoRepository.getItemStream(matricula)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        // Espera hasta que la operación asíncrona se complete
        profileResponse = deferred.await()

        profileResponse?.profileDetails?.matricula?.let { Log.e("OBTENIENDO REGISTRO", it) }
        return profileResponse?.profileDetails?.toItem()
    }

    //CALI FINALES
    suspend fun getCaliFinal(matricula: String): List<CalificacionDB> {
        return withContext(Dispatchers.IO) {
            CaliFinalesRepository.getAllItemsStream(matricula)
        }
    }

    //CALI UNIDAD
    suspend fun getCaliUnidad(matricula: String): List<CalificacionUnidadDB> {
        return withContext(Dispatchers.IO) {
            CaliPorUnidadRepository.getAllItemsStream(matricula)
        }
    }

    //CARGA ACADEMICA
    suspend fun getCargaAc(matricula: String): List<CargaAcademicaItemDB> {
        return withContext(Dispatchers.IO) {
            CargaAcademicaRepository.getAllItemsStream(matricula)
        }
    }

    //KARDEXITEM DB
    suspend fun getKardex(matricula: String): List<KardexItemDB> {
        return withContext(Dispatchers.IO) {
            KardexItemRepository.getAllItemsStream(matricula)
        }
    }

    //PROMEDIO DB
    suspend fun getPromedio1(matricula: String): PromedioDB {
        // Utiliza viewModelScope.async para obtener un Deferred

        val deferred = viewModelScope.async {
            PromedioRepository.getItemStream(matricula)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        return deferred.await().promedioDetails.toItem()
    }

    //WORKERS
    private val workManager = WorkManager.getInstance()

    //WORKER LOGIN
    // LiveData para observar el estado del login
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    // Función para establecer el resultado del login
    fun setLoginResult(successful: Boolean) {
        _loginResult.value = successful
    }

    fun loginWorkManager(matricula: String, pass: String){
        val inputData = workDataOf("matricula" to matricula, "password" to pass)
        val profileRequest = OneTimeWorkRequestBuilder<LoginWorker>()
            .setInputData(inputData)
            .build()

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(profileRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    // El worker terminó correctamente, actualiza LiveData
                    _loginResult.value = true
                }
            }

        // Enqueue el trabajo
        workManager.enqueue(profileRequest)
    }

    //WORKER CALIF UNIDAD
    // LiveData para observar el estado del login
    private val _califUResult = MutableLiveData<Boolean>()
    val califUResult: LiveData<Boolean>
        get() = _califUResult

    // Función para establecer el resultado del login
    fun setCalifUResult(successful: Boolean) {
        _califUResult.value = successful
    }

    fun califUWorkManager(matricula: String){
        val inputData = workDataOf("matricula" to matricula)
        val califURequest = OneTimeWorkRequestBuilder<CalifUnidadWorker>()
            .setInputData(inputData)
            .build()

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(califURequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    // El worker terminó correctamente, actualiza LiveData
                    _califUResult.value = true
                }
            }

        // Enqueue el trabajo
        workManager.enqueue(califURequest)
    }

    //WORKER CALIF FINAL
    // LiveData para observar el estado del login
    private val _califFResult = MutableLiveData<Boolean>()
    val califFResult: LiveData<Boolean>
        get() = _califFResult

    // Función para establecer el resultado del login
    fun setCalifFResult(successful: Boolean) {
        _califFResult.value = successful
    }

    fun califFWorkManager(matricula: String){
        val inputData = workDataOf("matricula" to matricula)
        val califFRequest = OneTimeWorkRequestBuilder<CalifFinalWorker>()
            .setInputData(inputData)
            .build()

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(califFRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    // El worker terminó correctamente, actualiza LiveData
                    _califFResult.value = true
                }
            }

        // Enqueue el trabajo
        workManager.enqueue(califFRequest)
    }

    //WORKER CARGA ACADEMICA
    // LiveData para observar el estado del login
    private val _cargaAcResult = MutableLiveData<Boolean>()
    val cargaAcResult: LiveData<Boolean>
        get() = _cargaAcResult

    // Función para establecer el resultado del login
    fun setCargaAcResult(successful: Boolean) {
        _cargaAcResult.value = successful
    }

    fun cargaAcWorkManager(matricula: String){
        val inputData = workDataOf("matricula" to matricula)
        val cargaAcRequest = OneTimeWorkRequestBuilder<CargaAcademicaWorker>()
            .setInputData(inputData)
            .build()

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(cargaAcRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    // El worker terminó correctamente, actualiza LiveData
                    _cargaAcResult.value = true
                }
            }

        // Enqueue el trabajo
        workManager.enqueue(cargaAcRequest)
    }

    //WORKER KARDEX
    // LiveData para observar el estado del login
    private val _kardexResult = MutableLiveData<Boolean>()
    val kardexResult: LiveData<Boolean>
        get() = _kardexResult

    // Función para establecer el resultado del login
    fun setKardexResult(successful: Boolean) {
        _kardexResult.value = successful
    }

    fun kardexWorkManager(matricula: String){
        val inputData = workDataOf("matricula" to matricula)
        val kardexRequest = OneTimeWorkRequestBuilder<KardexWorker>()
            .setInputData(inputData)
            .build()

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(kardexRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    // El worker terminó correctamente, actualiza LiveData
                    _kardexResult.value = true
                }
            }

        // Enqueue el trabajo
        workManager.enqueue(kardexRequest)
    }

    fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SicenetApplication)
                val siceRepository = application.container.SicenetRepository
                val accessRepository = application.container.AccessLoginResponseRepository
                val profileRepository = application.container.AlumnoAcademicoWithLineamientoRepository
                val caliFinalesRepository = application.container.CaliFinalesRepository
                val caliUnidadRepository = application.container.CaliPorUnidadRepository
                val CargaAcademicaRepository = application.container.CargaAcademicaRepository
                val KardexItemRepository = application.container.KardexItemRepository
                val PromedioRepository = application.container.PromedioRepository
                DataViewModel(SicenetRepository = siceRepository,
                    AccessLoginResponseRepository = accessRepository,
                    AlumnoAcademicoWithLineamientoRepository = profileRepository,
                    CaliFinalesRepository = caliFinalesRepository,
                    CaliPorUnidadRepository = caliUnidadRepository,
                    CargaAcademicaRepository= CargaAcademicaRepository,
                    KardexItemRepository= KardexItemRepository,
                    PromedioRepository= PromedioRepository)
            }
        }
    }
}




