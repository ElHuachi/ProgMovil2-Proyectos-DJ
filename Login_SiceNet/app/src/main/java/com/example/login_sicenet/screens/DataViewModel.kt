package com.example.login_sicenet.screens

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
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
import com.example.login_sicenet.model.AccessLoginResponseDB
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
import com.example.login_sicenet.model.CargaAcademica
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.Kardex
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.KardexItemDetails
import com.example.login_sicenet.model.KardexUiState
import com.example.login_sicenet.model.ProfileDetails
import com.example.login_sicenet.model.ProfileUiState
import com.example.login_sicenet.model.PromedioDB
import com.example.login_sicenet.model.PromedioDetails
import com.example.login_sicenet.model.PromedioUiState
import com.example.login_sicenet.model.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    var cargaAcademica: List<CargaAcademicaItem>? = null

    var nControl: String = ""
    var pass: String = ""
    var lineamiento: String = "2"

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
    fun updateUiStatProfile(profileDetails: ProfileDetails, AlumnoAcademicoResultDB: AlumnoAcademicoResultDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedProfileDetails = profileDetails.copy(matricula = AlumnoAcademicoResultDB.matricula, fechaReins = AlumnoAcademicoResultDB.fechaReins,
            estatus =  AlumnoAcademicoResultDB.estatus, modEducativo = AlumnoAcademicoResultDB.modEducativo, adeudo = AlumnoAcademicoResultDB.adeudo,
            urlFoto = AlumnoAcademicoResultDB.urlFoto, adeudoDescripcion = AlumnoAcademicoResultDB.adeudoDescripcion,
            inscrito = AlumnoAcademicoResultDB.inscrito, semActual = AlumnoAcademicoResultDB.semActual, cdtosActuales = AlumnoAcademicoResultDB.cdtosActuales,
            cdtosAcumulados = AlumnoAcademicoResultDB.cdtosAcumulados, especialidad = AlumnoAcademicoResultDB.especialidad,
            carrera = AlumnoAcademicoResultDB.carrera, lineamiento = AlumnoAcademicoResultDB.lineamiento, nombre = AlumnoAcademicoResultDB.nombre,
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

    suspend fun saveCaliFinales() {
        if (validateInputCaliFinales()) {
            // Guarda la peticion de acceso y obtén el ID.
            val califId = CaliFinalesRepository.insertItemAndGetId(caliFinUiState.califFinDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatCaliFinales(accessDetails: CalifFinDetails, CalificacionDB: CalificacionDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCaliFinDetails = accessDetails.copy(matricula = CalificacionDB.matricula, calif = CalificacionDB.calif,
            acred =  CalificacionDB.acred, grupo = CalificacionDB.grupo, materia = CalificacionDB.materia,
            observaciones = CalificacionDB.observaciones,fecha = currentDateTime)
        caliFinUiState = CaliFinalesUiState(califFinDetails = updatedCaliFinDetails, isEntryValid = validateInputCaliFinales())
    }

    //CALI UNIDAD
    var caliUnidadUiState by mutableStateOf(CaliUnidadUiState())
        private set
    private fun validateInputCaliUnidad(uiState: CalifUnidadDetails = caliUnidadUiState.califUnidadDetails): Boolean {
        return with(uiState) {
            fecha.isNotBlank()
        }
    }

    suspend fun saveCaliUnidad() {
        if (validateInputCaliUnidad()) {
            // Guarda la peticion de acceso y obtén el ID.
            val califId = CaliPorUnidadRepository.insertItemAndGetId(caliUnidadUiState.califUnidadDetails.toItem())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiStatCaliUnidad(accessDetails: CalifUnidadDetails, CalificacionUnidadDB: CalificacionUnidadDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCaliUnidadDetails = accessDetails.copy(matricula = CalificacionUnidadDB.matricula, observaciones = CalificacionUnidadDB.observaciones,
            unidadesActivas =  CalificacionUnidadDB.unidadesActivas, grupo = CalificacionUnidadDB.grupo, materia = CalificacionUnidadDB.materia,
            c1 = CalificacionUnidadDB.c1, c2 = CalificacionUnidadDB.c2,c3 = CalificacionUnidadDB.c3,c4 = CalificacionUnidadDB.c4,
            c5 = CalificacionUnidadDB.c5, c6 = CalificacionUnidadDB.c6,c7 = CalificacionUnidadDB.c7,c8 = CalificacionUnidadDB.c8,
            c9 = CalificacionUnidadDB.c9, c10 = CalificacionUnidadDB.c10,c11 = CalificacionUnidadDB.c11,c12 = CalificacionUnidadDB.c12,
            c13 = CalificacionUnidadDB.c13, fecha = currentDateTime)
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
    fun updateUiStatCaliUnidad(accessDetails: CargaAcDetails, CargaAcademicaItemDB: CargaAcademicaItemDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedCargaAcDetails = accessDetails.copy(matricula = CargaAcademicaItemDB.matricula, observaciones = CargaAcademicaItemDB.observaciones,
            semipresencial =  CargaAcademicaItemDB.semipresencial, grupo = CargaAcademicaItemDB.grupo, materia = CargaAcademicaItemDB.materia,
            docente = CargaAcademicaItemDB.docente, clvOficial = CargaAcademicaItemDB.clvOficial, sabado = CargaAcademicaItemDB.sabado, viernes = CargaAcademicaItemDB.viernes,
            jueves = CargaAcademicaItemDB.jueves, miercoles = CargaAcademicaItemDB.miercoles, martes = CargaAcademicaItemDB.martes, lunes = CargaAcademicaItemDB.lunes,
            estadoMateria = CargaAcademicaItemDB.estadoMateria, creditosMateria = CargaAcademicaItemDB.creditosMateria,fecha = currentDateTime)
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
    fun updateUiStateKardex(accessDetails: KardexItemDetails, KardexItemDB: KardexItemDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedKardexDetails = accessDetails.copy(matricula = KardexItemDB.matricula, s3 = KardexItemDB.s3,
            p3 =  KardexItemDB.p3, a3 = KardexItemDB.a3, materia = KardexItemDB.materia,
            s2 = KardexItemDB.s2, p2 = KardexItemDB.s2, a2 = KardexItemDB.a2, p1 = KardexItemDB.p1,
            s1 = KardexItemDB.s1, a1 = KardexItemDB.a1, clvMat = KardexItemDB.clvMat, clvOfiMat = KardexItemDB.clvOfiMat,
            cdts = KardexItemDB.cdts, calif = KardexItemDB.calif, acred = KardexItemDB.acred, fecha = currentDateTime)
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
    fun updateUiStateKardex(accessDetails: PromedioDetails, PromedioDB: PromedioDB) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedPromedioDetails = accessDetails.copy(matricula = PromedioDB.matricula, promedioGral = PromedioDB.promedioGral,
            cdtsAcum =  PromedioDB.cdtsAcum, cdtsPlan = PromedioDB.cdtsPlan, matAprobadas = PromedioDB.matAprobadas,
            matCursadas = PromedioDB.matCursadas, avanceCdts = PromedioDB.avanceCdts, fecha = currentDateTime)
        promedioUiState = PromedioUiState(promedioDetails = updatedPromedioDetails, isEntryValid = validateInputKardex())
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




