package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificacionesUnidad",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CalificacionUnidadDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int=0,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "observaciones")
    val observaciones: String,

    @ColumnInfo(name = "C13")
    val c13: String?,

    @ColumnInfo(name = "C12")
    val c12: String?,

    @ColumnInfo(name = "C11")
    val c11: String?,

    @ColumnInfo(name = "C10")
    val c10: String?,

    @ColumnInfo(name = "C9")
    val c9: String?,

    @ColumnInfo(name = "C8")
    val c8: String?,

    @ColumnInfo(name = "C7")
    val c7: String?,

    @ColumnInfo(name = "C6")
    val c6: String?,

    @ColumnInfo(name = "C5")
    val c5: String?,

    @ColumnInfo(name = "C4")
    val c4: String?,

    @ColumnInfo(name = "C3")
    val c3: String?,

    @ColumnInfo(name = "C2")
    val c2: String?,

    @ColumnInfo(name = "C1")
    val c1: String?,

    @ColumnInfo(name = "unidadesActivas")
    val unidadesActivas: String,

    @ColumnInfo(name = "materia")
    val materia: String,

    @ColumnInfo(name = "grupo")
    val grupo: String,

    @ColumnInfo(name = "fecha")
    val fecha: String
)

//CALIF FINALES DB
data class CaliUnidadUiState(
    val califUnidadDetails: CalifUnidadDetails = CalifUnidadDetails(),
    val isEntryValid: Boolean = true
)

data class CalifUnidadDetails(
    val id: Int = 0,
    val matricula: String? = null,
    val c1: String? = "",
    val c2: String? = "",
    val c3: String? = "",
    val c4: String? = "",
    val c5: String? = "",
    val c6: String? = "",
    val c7: String? = "",
    val c8: String? = "",
    val c9: String? = "",
    val c10: String? = "",
    val c11: String? = "",
    val c12: String? = "",
    val c13: String? = "",
    val observaciones: String="",
    val unidadesActivas: String="",
    val materia: String="",
    val grupo: String="",
    val fecha: String = "",
)


fun CalifUnidadDetails.toItem(): CalificacionUnidadDB = CalificacionUnidadDB(
    id = id,
    matricula = matricula,
    observaciones = observaciones,
    unidadesActivas = unidadesActivas,
    grupo = grupo,
    materia = materia,
    c1 = c1,
    c2 = c2,
    c3 = c3,
    c4 = c4,
    c5 = c5,
    c6 = c6,
    c7 = c7,
    c8 = c8,
    c9 = c9,
    c10 = c10,
    c11 = c11,
    c12 = c12,
    c13 = c13,
    fecha=fecha
)

fun CalificacionUnidadDB.toItemUiState(isEntryValid: Boolean = false): CaliUnidadUiState = CaliUnidadUiState(
    califUnidadDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun CalificacionUnidadDB.toItemDetails(): CalifUnidadDetails = CalifUnidadDetails(
    id = id,
    matricula = matricula,
    observaciones = observaciones,
    unidadesActivas = unidadesActivas,
    grupo = grupo,
    materia = materia,
    c1 = c1,
    c2 = c2,
    c3 = c3,
    c4 = c4,
    c5 = c5,
    c6 = c6,
    c7 = c7,
    c8 = c8,
    c9 = c9,
    c10 = c10,
    c11 = c11,
    c12 = c12,
    c13 = c13,
    fecha=fecha
)
