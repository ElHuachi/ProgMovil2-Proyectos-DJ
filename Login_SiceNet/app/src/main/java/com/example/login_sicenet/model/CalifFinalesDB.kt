package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificacionesFinales",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CalificacionDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int=0,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "calif")
    val calif: Int,

    @ColumnInfo(name = "acred")
    val acred: String,

    @ColumnInfo(name = "grupo")
    val grupo: String,

    @ColumnInfo(name = "materia")
    val materia: String,

    @ColumnInfo(name = "observaciones")
    val observaciones: String,

    @ColumnInfo(name = "fecha")
    val fecha: String,

)

//CALIF FINALES DB
data class CaliFinalesUiState(
    val califFinDetails: CalifFinDetails = CalifFinDetails(),
    val isEntryValid: Boolean = true
)

data class CalifFinDetails(
    val id: Int = 0,
    val matricula: String? = null,
    val calif: Int=0,
    val acred: String="",
    val grupo: String="",
    val materia: String="",
    val observaciones: String="",
    val fecha: String = ""
)


fun CalifFinDetails.toItem(): CalificacionDB = CalificacionDB(
    id = id,
    matricula = matricula,
    calif = calif,
    acred = acred,
    grupo = grupo,
    materia = materia,
    observaciones = observaciones,
    fecha=fecha
)

fun CalificacionDB.toItemUiState(isEntryValid: Boolean = false): CaliFinalesUiState = CaliFinalesUiState(
    califFinDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun CalificacionDB.toItemDetails(): CalifFinDetails = CalifFinDetails(
    id = id,
    matricula = matricula,
    calif = calif,
    acred = acred,
    grupo = grupo,
    materia = materia,
    observaciones = observaciones,
    fecha=fecha
)
