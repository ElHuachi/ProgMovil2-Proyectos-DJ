package com.example.login_sicenet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlumnoAcademicoResult(
    val fechaReins: String,
    @SerialName("modEducativo")
    val modEducativo: Int,
    val adeudo: Boolean,
    val urlFoto: String,
    val adeudoDescripcion: String,
    val inscrito: Boolean,
    val estatus: String,
    @SerialName("semActual")
    val semActual: Int,
    @SerialName("cdtosAcumulados")
    val cdtosAcumulados: Int,
    @SerialName("cdtosActuales")
    val cdtosActuales: Int,
    val especialidad: String,
    val carrera: String,
    val lineamiento: Int,
    val nombre: String,
    val matricula: String
)