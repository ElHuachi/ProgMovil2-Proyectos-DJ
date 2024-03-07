package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeCalif @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var bodyCaliFinal: BodyCaliFinal = BodyCaliFinal()
)

@Root(name = "Body", strict = false)
data class BodyCaliFinal @JvmOverloads constructor(
    @field:Element(name = "getAllCalifFinalByAlumnosResponse", required = false)
    var allCalifFinalByAlumnosResponse: AllCalifFinalByAlumnosResponse = AllCalifFinalByAlumnosResponse()
)

@Root(name = "getAllCalifFinalByAlumnosResponse", strict = false)
data class AllCalifFinalByAlumnosResponse @JvmOverloads constructor(
    @field:Element(name = "getAllCalifFinalByAlumnosResult", required = false)
    var getAllCalifFinalByAlumnosResult: String = ""
)

@Serializable
data class Calificacion(
    @SerialName("calif")
    val calif: Int,

    @SerialName("acred")
    val acred: String,

    @SerialName("grupo")
    val grupo: String,

    @SerialName("materia")
    val materia: String,

    @SerialName("Observaciones")
    val observaciones: String
)



