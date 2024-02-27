package com.example.login_sicenet.screens

import android.content.Context
import android.webkit.CookieManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.model.AccesoLoginResult
import com.example.login_sicenet.model.AlumnoAcademicoResult
import com.example.login_sicenet.model.Calificacion
import com.example.login_sicenet.model.CalificacionUnidad
import com.example.login_sicenet.model.CargaAcademica
import com.example.login_sicenet.model.CargaAcademicaItem
import com.example.login_sicenet.model.Kardex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed interface SiceUiState {
    object Success : SiceUiState
    object Error : SiceUiState
    object Loading : SiceUiState
}

class DataViewModel(private val SicenetRepository: SicenetRepository) : ViewModel() {
    var accesoLoginResult: AccesoLoginResult? = null
    var alumnoAcademicoResult: AlumnoAcademicoResult? = null
    var califFinales: List<Calificacion>? = null
    var calfUnidades: List<CalificacionUnidad>? = null
    var kardex: Kardex? = null
    var cargaAcademica: List<CargaAcademicaItem>? = null

    var nControl: String = ""
    var pass: String = ""
    private lateinit var myContext: Context

    // Método para asignar el contexto
    fun setContext(context: Context) {
        myContext = context
    }

    var siceUiState: SiceUiState by mutableStateOf(SiceUiState.Loading)
        private set

    init {
        login()
    }
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SicenetApplication)
                val siceRepository = application.container.SicenetRepository
                DataViewModel(SicenetRepository = siceRepository)
            }
        }
    }
}