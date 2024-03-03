package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeCargaAcademica @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var bodyCargaAcademica: BodyCargaAcademica = BodyCargaAcademica()
)

@Root(name = "Body", strict = false)
data class BodyCargaAcademica @JvmOverloads constructor(
    @field:Element(name = "getCargaAcademicaByAlumnoResponse", required = false)
    var getCargaAcademicaByAlumnoResponse: GetCargaAcademicaByAlumnoResponse = GetCargaAcademicaByAlumnoResponse()
)

@Root(name = "getCargaAcademicaByAlumnoResponse", strict = false)
data class GetCargaAcademicaByAlumnoResponse @JvmOverloads constructor(
    @field:Element(name = "getCargaAcademicaByAlumnoResult", required = false)
    var getCargaAcademicaByAlumnoResult: String = ""
)

@Serializable
data class CargaAcademicaItem(
    @SerialName("Semipresencial")
    val semipresencial: String?,

    @SerialName("Observaciones")
    val observaciones: String?,

    @SerialName("Docente")
    val docente: String,

    @SerialName("clvOficial")
    val clvOficial: String,

    @SerialName("Sabado")
    val sabado: String?,

    @SerialName("Viernes")
    val viernes: String?,

    @SerialName("Jueves")
    val jueves: String?,

    @SerialName("Miercoles")
    val miercoles: String?,

    @SerialName("Martes")
    val martes: String?,

    @SerialName("Lunes")
    val lunes: String?,

    @SerialName("EstadoMateria")
    val estadoMateria: String,

    @SerialName("CreditosMateria")
    val creditosMateria: Int,

    @SerialName("Materia")
    val materia: String,

    @SerialName("Grupo")
    val grupo: String
)

@Serializable
data class CargaAcademica(
    val lstCargaAcademica: List<CargaAcademicaItem>
)


