package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeLogin @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var bodyLogin: BodyLogin = BodyLogin()
)

@Root(name = "Body", strict = false)
data class BodyLogin @JvmOverloads constructor(
    @field:Element(name = "accesoLoginResponse", required = false)
    var accesoLoginResponse: AccesoLoginResponse = AccesoLoginResponse()
)

@Root(name = "accesoLoginResponse", strict = false)
data class AccesoLoginResponse @JvmOverloads constructor(
    @field:Element(name = "accesoLoginResult", required = false)
    var accesoLoginResult: String? = null
)

@Serializable
data class AccesoLoginResult(
    var acceso: Boolean? = null,
    var estatus: String? = null,
    var tipoUsuario: Int? = null,
    var contrasenia: String? = null,
    var matricula: String? = null
)