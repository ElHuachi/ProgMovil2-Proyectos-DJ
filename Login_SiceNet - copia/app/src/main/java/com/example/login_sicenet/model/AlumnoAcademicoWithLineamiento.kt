package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class Envelope @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var body: Body = Body()
)

@Root(name = "Body", strict = false)
data class Body @JvmOverloads constructor(
    @field:Element(name = "getAlumnoAcademicoWithLineamientoResponse", required = false)
    var getAlumnoAcademicoWithLineamientoResponse: GetAlumnoAcademicoWithLineamientoResponse = GetAlumnoAcademicoWithLineamientoResponse()
)

@Root(name = "getAlumnoAcademicoWithLineamientoResponse", strict = false)
data class GetAlumnoAcademicoWithLineamientoResponse @JvmOverloads constructor(
    @field:Element(name = "getAlumnoAcademicoWithLineamientoResult", required = false)
    var getAlumnoAcademicoWithLineamientoResult: String = ""
)

@Serializable
data class AlumnoAcademicoResult(
    val fechaReins: String? = null,
    @SerialName("modEducativo")
    val modEducativo: Int? = null,
    val adeudo: Boolean? = null,
    val urlFoto: String? = null,
    val adeudoDescripcion: String? = null,
    val inscrito: Boolean? = null,
    val estatus: String? = null,
    @SerialName("semActual")
    val semActual: Int? = null,
    @SerialName("cdtosAcumulados")
    val cdtosAcumulados: Int? = null,
    @SerialName("cdtosActuales")
    val cdtosActuales: Int? = null,
    val especialidad: String? = null,
    val carrera: String? = null,
    val lineamiento: Int? = null,
    val nombre: String? = null,
    val matricula: String? = null
)