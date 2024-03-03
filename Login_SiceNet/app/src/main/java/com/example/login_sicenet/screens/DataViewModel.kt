package com.example.login_sicenet.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
import com.example.login_sicenet.model.Kardex
import com.example.login_sicenet.model.KardexItem
import com.example.login_sicenet.model.KardexItemDetails
import com.example.login_sicenet.model.KardexUiState
import com.example.login_sicenet.model.ProfileDetails
import com.example.login_sicenet.model.ProfileUiState
import com.example.login_sicenet.model.Promedio
import com.example.login_sicenet.model.PromedioDetails
import com.example.login_sicenet.model.PromedioUiState
import com.example.login_sicenet.model.toItem
import com.example.login_sicenet.model.toItemDetails
import com.example.login_sicenet.model.toItemUiState
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
    var accesoLoginResult: AccesoLoginResult? = null
    var alumnoAcademicoResult: AlumnoAcademicoResult? = null
    var califFinales: List<Calificacion>? = null
    var califUnidades: List<CalificacionUnidad>? = null
    var kardex: Kardex? = null
    var promedio: Promedio? = null
    var cargaAcademica: List<CargaAcademicaItem>? = null

    var nControl: String = ""
    var pass: String = ""
    var lineamiento: String = "2"
    var internet: Boolean = true

    var perfilDB: AlumnoAcademicoResultDB? = null
    var caliUnidadDB: List<CalificacionUnidadDB>? = null
    var caliUnidadDB1: CalificacionUnidadDB? = null
    var caliFinalDB1: CalificacionDB? = null

    var siceUiState: SiceUiState by mutableStateOf(SiceUiState.Loading)
        private set

//    init {
//        login()
//    }

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

    fun performLoginAndFetchAcademicProfile() {
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
    var accesoUiState by mutableStateOf(AccesoUiState())
        private set

    var accesoResponse by mutableStateOf(AccesoUiState())
        private set

    suspend fun getAccesoExistente(matricula: String): Boolean? {
        var accesoResponse: AccesoUiState? = null

        // Utiliza viewModelScope.async para obtener un Deferred
        val deferred = viewModelScope.async {
            AccessLoginResponseRepository.getItemStream(matricula)
                .firstOrNull()
                ?.toItemUiState(true)
        }

        return deferred.await() != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateAccessDB() {
        var fecha  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        AccessLoginResponseRepository.updateItemQuery(nControl,fecha)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteAccessDB(matricula: String) {
        AccessLoginResponseRepository.deleteItem(matricula)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStateAccessU(accessDetails: AccesoDetails) {
        accessDetails.fecha  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedAccesoDetails = accessDetails.copy(fecha = accessDetails.fecha)
        accesoResponse = AccesoUiState(accesoDetails = updatedAccesoDetails, isEntryValid = validateInputAccess(accessDetails))
    }

    private fun validateInputAccess(uiState: AccesoDetails = accesoUiState.accesoDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    suspend fun saveAccessResult() {
        if (validateInputAccess()) {
            // Guarda la peticion de acceso y obtén el ID.
            val accessId = AccessLoginResponseRepository.insertItemAndGetId(accesoUiState.accesoDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStateAccess(accessDetails: AccesoDetails, AccessLoginResult: AccesoLoginResult) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedAccesoDetails = accessDetails.copy(acceso = AccessLoginResult.acceso, matricula = AccessLoginResult.matricula,
            estatus =  AccessLoginResult.estatus, tipo_usuario = AccessLoginResult.tipoUsuario,
            contrasenia = AccessLoginResult.contrasenia, fecha = currentDateTime)
        accesoUiState = AccesoUiState(accesoDetails = updatedAccesoDetails, isEntryValid = validateInputAccess(updatedAccesoDetails))
    }

    //PROFILE
    var profileUiState by mutableStateOf(ProfileUiState())
        private set

    suspend fun saveProfileResult() {
        // Guarda la peticion de acceso y obtén el ID.
        val profileId = AlumnoAcademicoWithLineamientoRepository.insertItemAndGetId(profileUiState.profileDetails.toItem())

    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateProfileDB() {
        var fecha  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        AlumnoAcademicoWithLineamientoRepository.updateItemQuery(nControl,fecha)
    }

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

    suspend fun getCaliUnidad(matricula: String): List<CalificacionUnidadDB> {
        // Utiliza viewModelScope.async para obtener un Deferred

        return CaliPorUnidadRepository.getAllItemsStream(matricula)
            .toList()
            .flatten()
    }

    suspend fun getCaliUnidad1(matricula: String): CalificacionUnidadDB {
        // Utiliza viewModelScope.async para obtener un Deferred

        val deferred = viewModelScope.async {
            CaliPorUnidadRepository.getItemStream(matricula)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        return deferred.await().califUnidadDetails.toItem()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStateProfile(accessDetails: ProfileDetails) {
        accessDetails.fecha  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedAccesoDetails = accessDetails.copy(fecha = accessDetails.fecha)
        profileUiState = ProfileUiState(profileDetails = updatedAccesoDetails, isEntryValid = true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun actualizar() {
        val currentItem = profileUiState.profileDetails.toItem()
        AlumnoAcademicoWithLineamientoRepository.updateItem(currentItem.copy(fecha = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatProfile(profileDetails: ProfileDetails, AlumnoAcademicoResult: AlumnoAcademicoResult) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedProfileDetails = profileDetails.copy(matricula = AlumnoAcademicoResult.matricula, fechaReins = AlumnoAcademicoResult.fechaReins,
            estatus =  AlumnoAcademicoResult.estatus, modEducativo = AlumnoAcademicoResult.modEducativo, adeudo = AlumnoAcademicoResult.adeudo,
            urlFoto = AlumnoAcademicoResult.urlFoto, adeudoDescripcion = AlumnoAcademicoResult.adeudoDescripcion,
            inscrito = AlumnoAcademicoResult.inscrito, semActual = AlumnoAcademicoResult.semActual, cdtosActuales = AlumnoAcademicoResult.cdtosActuales,
            cdtosAcumulados = AlumnoAcademicoResult.cdtosAcumulados, especialidad = AlumnoAcademicoResult.especialidad,
            carrera = AlumnoAcademicoResult.carrera, lineamiento = AlumnoAcademicoResult.lineamiento, nombre = AlumnoAcademicoResult.nombre,
            fecha = currentDateTime)
        profileUiState = ProfileUiState(profileDetails = updatedProfileDetails)
    }

    //CALI FINALES
    var caliFinUiState by mutableStateOf(CaliFinalesUiState())
        private set
    private fun validateInputCaliFinales(uiState: CalifFinDetails = caliFinUiState.califFinDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatCaliFinales(accessDetails: CalifFinDetails, Calificacion: Calificacion, matricula: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCaliFinDetails = accessDetails.copy(matricula = matricula, calif = Calificacion.calif,
            acred =  Calificacion.acred, grupo = Calificacion.grupo, materia = Calificacion.materia,
            observaciones = Calificacion.observaciones,fecha = currentDateTime)
        caliFinUiState = CaliFinalesUiState(califFinDetails = updatedCaliFinDetails, isEntryValid = validateInputCaliFinales())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveCaliFinal() {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        for(cali in califFinales!!){
            val calif = CalificacionDB(
                matricula = nControl,
                observaciones = cali.observaciones,
                acred = cali.acred,
                calif = cali.calif,
                materia = cali.materia,
                grupo = cali.grupo,
                fecha= currentDateTime
            )
            val califId = CaliFinalesRepository.insertItemAndGetId(calif)
        }
    }

    suspend fun getCaliFinal1(matricula: String): CalificacionDB {
        // Utiliza viewModelScope.async para obtener un Deferred

        val deferred = viewModelScope.async {
            CaliFinalesRepository.getItemStream(matricula)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        return deferred.await().califFinDetails.toItem()
    }

    suspend fun getCaliFinalExistente(matricula: String): Boolean? {
        // Utiliza viewModelScope.async para obtener un Deferred
        val deferred = viewModelScope.async {
            CaliFinalesRepository.getItemStream(matricula)
                .firstOrNull()
                ?.toItemUiState(true)
        }

        return deferred.await() != null
    }

    //CALI UNIDAD
    var caliUnidadUiState by mutableStateOf(CaliUnidadUiState())
        private set

    suspend fun getCaliUnidadExistente(matricula: String): Boolean? {
        // Utiliza viewModelScope.async para obtener un Deferred
        val deferred = viewModelScope.async {
            CaliPorUnidadRepository.getItemStream(matricula)
                .firstOrNull()
                ?.toItemUiState(true)
        }

        return deferred.await() != null
    }

    private fun validateInputCaliUnidad(uiState: CalifUnidadDetails = caliUnidadUiState.califUnidadDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveCaliUnidad() {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        for(cali in califUnidades!!){
            val calif = CalificacionUnidadDB(
                matricula = nControl,
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
            val califId = CaliPorUnidadRepository.insertItemAndGetId(calif)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateCaliUnidad() {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        for(cali in califUnidades!!){
            val calif = CalificacionUnidadDB(
                matricula = nControl,
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
            val califId = CaliPorUnidadRepository.updateQuery(nControl,currentDateTime)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatCaliUnidad(accessDetails: CalifUnidadDetails, CalificacionUnidad: CalificacionUnidad, matricula: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCaliUnidadDetails = accessDetails.copy(matricula = matricula, observaciones = CalificacionUnidad.observaciones,
            unidadesActivas =  CalificacionUnidad.unidadesActivas, grupo = CalificacionUnidad.grupo, materia = CalificacionUnidad.materia,
            c1 = CalificacionUnidad.c1, c2 = CalificacionUnidad.c2,c3 = CalificacionUnidad.c3,c4 = CalificacionUnidad.c4,
            c5 = CalificacionUnidad.c5, c6 = CalificacionUnidad.c6,c7 = CalificacionUnidad.c7,c8 = CalificacionUnidad.c8,
            c9 = CalificacionUnidad.c9, c10 = CalificacionUnidad.c10,c11 = CalificacionUnidad.c11,c12 = CalificacionUnidad.c12,
            c13 = CalificacionUnidad.c13, fecha = currentDateTime)
        caliUnidadUiState = CaliUnidadUiState(califUnidadDetails = updatedCaliUnidadDetails, isEntryValid = validateInputCaliUnidad())
    }

    //CARGA ACADEMICA
    var cargaAcUiState by mutableStateOf(CargaAcUiState())
        private set
    private fun validateInputCargaAc(uiState: CargaAcDetails = cargaAcUiState.cargaAcDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    suspend fun saveCargaAc() {
        if (validateInputCaliUnidad()) {
            // Guarda la peticion de acceso y obtén el ID.
            val cargaId = CargaAcademicaRepository.insertItemAndGetId(cargaAcUiState.cargaAcDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatCargaAc(accessDetails: CargaAcDetails, CargaAcademicaItem: CargaAcademicaItem, matricula: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCargaAcDetails = accessDetails.copy(matricula = matricula, observaciones = CargaAcademicaItem.observaciones,
            semipresencial =  CargaAcademicaItem.semipresencial, grupo = CargaAcademicaItem.grupo, materia = CargaAcademicaItem.materia,
            docente = CargaAcademicaItem.docente, clvOficial = CargaAcademicaItem.clvOficial, sabado = CargaAcademicaItem.sabado, viernes = CargaAcademicaItem.viernes,
            jueves = CargaAcademicaItem.jueves, miercoles = CargaAcademicaItem.miercoles, martes = CargaAcademicaItem.martes, lunes = CargaAcademicaItem.lunes,
            estadoMateria = CargaAcademicaItem.estadoMateria, creditosMateria = CargaAcademicaItem.creditosMateria,fecha = currentDateTime)
        cargaAcUiState = CargaAcUiState(cargaAcDetails = updatedCargaAcDetails, isEntryValid = validateInputCaliUnidad())
    }

    //KARDEXITEM DB
    var kardexUiState by mutableStateOf(KardexUiState())
        private set
    private fun validateInputKardex(uiState: KardexItemDetails = kardexUiState.kardexItemDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    suspend fun saveKardex() {
        if (validateInputKardex()) {
            // Guarda la peticion de acceso y obtén el ID.
            val kardexId = KardexItemRepository.insertItemAndGetId(kardexUiState.kardexItemDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStateKardex(accessDetails: KardexItemDetails, KardexItem: KardexItem, matricula: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedKardexDetails = accessDetails.copy(matricula = matricula, s3 = KardexItem.s3,
            p3 =  KardexItem.p3, a3 = KardexItem.a3, materia = KardexItem.materia,
            s2 = KardexItem.s2, p2 = KardexItem.s2, a2 = KardexItem.a2, p1 = KardexItem.p1,
            s1 = KardexItem.s1, a1 = KardexItem.a1, clvMat = KardexItem.clvMat, clvOfiMat = KardexItem.clvOfiMat,
            cdts = KardexItem.cdts, calif = KardexItem.calif, acred = KardexItem.acred, fecha = currentDateTime)
        kardexUiState = KardexUiState(kardexItemDetails = updatedKardexDetails, isEntryValid = validateInputKardex())
    }

    //PROMEDIO DB
    var promedioUiState by mutableStateOf(PromedioUiState())
        private set
    private fun validateInputPromedio(uiState: PromedioDetails = promedioUiState.promedioDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    suspend fun savePromedio() {
        if (validateInputKardex()) {
            // Guarda la peticion de acceso y obtén el ID.
            val promedioId = PromedioRepository.insertItemAndGetId(promedioUiState.promedioDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatePromedio(accessDetails: PromedioDetails, Promedio: Promedio, matricula: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedPromedioDetails = accessDetails.copy(matricula = matricula, promedioGral = Promedio.promedioGral,
            cdtsAcum =  Promedio.cdtsAcum, cdtsPlan = Promedio.cdtsPlan, matAprobadas = Promedio.matAprobadas,
            matCursadas = Promedio.matCursadas, avanceCdts = Promedio.avanceCdts, fecha = currentDateTime)
        promedioUiState = PromedioUiState(promedioDetails = updatedPromedioDetails, isEntryValid = validateInputPromedio())
    }

    //WORKERS

    // LiveData que se actualizará cuando el Worker haya terminado
//    private val _navigate = MutableLiveData<Boolean>()
//    val navigate: LiveData<Boolean> get() = _navigate
//
//    val sicenetRepositorySerializer = SicenetRepositorySerializer(Gson())
//    val serializedRepository = sicenetRepositorySerializer.serialize(SicenetRepository)
//
//    val accessLoginRepositorySerializer = AccessLoginRepositorySerializer(Gson())
//    val serializedAccessRepository = accessLoginRepositorySerializer.serialize(AccessLoginResponseRepository)
//
//    val profileRepositorySerializer = ProfileRepositorySerializer(Gson())
//    val serializedProfileRepository = profileRepositorySerializer.serialize(AlumnoAcademicoWithLineamientoRepository)
//
//    val accesoDetailsSerializer = AccesoDetailsSerializer(Gson())
//   // val serializedAccesoDetails = accesoDetailsSerializer.serialize(accesoUiState.accesoDetails)
//
//    val profileDetailsSerializer = ProfileDetailsSerializer(Gson())
//    //val serializedProfileDetails = profileDetailsSerializer.serialize(profileUiState.profileDetails)
//
//    val serializedAccesoDetails = AccesoDetailsSerializer(Gson()).serialize(accesoUiState.accesoDetails)
//    val serializedProfileDetails = ProfileDetailsSerializer(Gson()).serialize(profileUiState.profileDetails)
//
//    fun startSyncProcess(context: Context): LiveData<WorkInfo> {
//        val inputData = Data.Builder()
//            .putString("nControl", nControl)
//            .putString("pass", pass)
//            .putString("serializedRepository", serializedRepository)
//            .putString("serializedAccessRepository", serializedAccessRepository)
//            .putString("serializedProfileRepository", serializedProfileRepository)
//            .putString("serializedAccesoDetails", serializedAccesoDetails)
//            .putString("serializedProfileDetails", serializedProfileDetails)
//            .build()
//
//        val loginRequest = OneTimeWorkRequestBuilder<LoginWorker>()
//            .setInputData(inputData)
//            .build()
//
//        val workManager = WorkManager.getInstance(context)
//        workManager.enqueue(loginRequest)
//
//        // Devuelve un LiveData que emite los cambios en los datos del Worker
//        return workManager.getWorkInfoByIdLiveData(loginRequest.id)
//    }
//
//    fun resetNavigate() {
//        _navigate.value = false
//    }

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




