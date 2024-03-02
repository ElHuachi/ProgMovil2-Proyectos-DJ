package com.example.login_sicenet.screens

import android.webkit.CookieManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.login_sicenet.SicenetApplication
import com.example.login_sicenet.data.SicenetRepository
import com.example.login_sicenet.model.AlumnoAcademicoResult
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface SiceUiState {
    data class Success(val perfil: String) : SiceUiState
    object Error : SiceUiState
    object Loading : SiceUiState
}

class DataViewModel(private val SicenetRepository: SicenetRepository) : ViewModel() {
    var alumnoAcademicoResult: AlumnoAcademicoResult? = null

    var siceUiState: SiceUiState by mutableStateOf(SiceUiState.Loading)
        private set

    init {
        getAcademicProfile()
    }

    fun getAcademicProfile(){
        viewModelScope.launch {
            siceUiState = SiceUiState.Loading
            siceUiState = try {
                val result = SicenetRepository.getPerfilAcademico()
                SiceUiState.Success(
                    "Success: ${result} "
                )
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