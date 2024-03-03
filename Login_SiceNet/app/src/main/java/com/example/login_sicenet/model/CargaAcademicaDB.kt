package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "carga_academica_items",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CargaAcademicaItemDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int=0,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "semipresencial")
    val semipresencial: String?,

    @ColumnInfo(name = "observaciones")
    val observaciones: String?,

    @ColumnInfo(name = "docente")
    val docente: String?,

    @ColumnInfo(name = "clvOficial")
    val clvOficial: String?,

    @ColumnInfo(name = "sabado")
    val sabado: String?,

    @ColumnInfo(name = "viernes")
    val viernes: String?,

    @ColumnInfo(name = "jueves")
    val jueves: String?,

    @ColumnInfo(name = "miercoles")
    val miercoles: String?,

    @ColumnInfo(name = "martes")
    val martes: String?,

    @ColumnInfo(name = "lunes")
    val lunes: String?,

    @ColumnInfo(name = "estadoMateria")
    val estadoMateria: String?,

    @ColumnInfo(name = "creditosMateria")
    val creditosMateria: Int?,

    @ColumnInfo(name = "materia")
    val materia: String?,

    @ColumnInfo(name = "grupo")
    val grupo: String?,

    @ColumnInfo(name = "fecha")
    val fecha: String
)

//CALIF FINALES DB
data class CargaAcUiState(
    val cargaAcDetails: CargaAcDetails = CargaAcDetails(),
    val isEntryValid: Boolean = true
)

data class CargaAcDetails(
    val id: Int = 0,
    val matricula: String? = null,
    val semipresencial: String? = "",
    val docente: String? = "",
    val clvOficial: String? = "",
    val sabado: String? = "",
    val viernes: String? = "",
    val jueves: String? = "",
    val miercoles: String? = "",
    val martes: String? = "",
    val lunes: String? = "",
    val estadoMateria: String? = "",
    val creditosMateria: Int? = 0,
    val observaciones: String?="",
    val materia: String?="",
    val grupo: String?="",
    val fecha: String = "",
)


fun CargaAcDetails.toItem(): CargaAcademicaItemDB = CargaAcademicaItemDB(
    id = id,
    matricula = matricula,
    observaciones = observaciones,
    semipresencial = semipresencial,
    grupo = grupo,
    materia = materia,
    docente = docente,
    clvOficial = clvOficial,
    sabado = sabado,
    viernes = viernes,
    jueves = jueves,
    miercoles = miercoles,
    martes = martes,
    lunes = lunes,
    estadoMateria = estadoMateria,
    creditosMateria = creditosMateria,
    fecha=fecha
)

fun CargaAcademicaItemDB.toItemUiState(isEntryValid: Boolean = false): CargaAcUiState = CargaAcUiState(
    cargaAcDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun CargaAcademicaItemDB.toItemDetails(): CargaAcDetails = CargaAcDetails(
    id = id,
    matricula = matricula,
    observaciones = observaciones,
    semipresencial = semipresencial,
    grupo = grupo,
    materia = materia,
    docente = docente,
    clvOficial = clvOficial,
    sabado = sabado,
    viernes = viernes,
    jueves = jueves,
    miercoles = miercoles,
    martes = martes,
    lunes = lunes,
    estadoMateria = estadoMateria,
    creditosMateria = creditosMateria,
    fecha=fecha
)
