package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessLoginResponse(
    @SerialName("acceso") val access: Boolean,
    @SerialName("estatus") val status: String,
    @SerialName("tipoUsuario") val userType: Int,
    @SerialName("contrasenia") val password: String,
    @SerialName("matricula") val matricula: String
)