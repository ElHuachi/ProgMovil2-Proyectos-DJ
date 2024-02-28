package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificacionesUnidad",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CalificacionUnidadDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "calificacion_unidad_id")
    val calificacionUnidadId: Long = 0,

    @ColumnInfo(name = "nControl")
    val nControl: String = "",

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
    val grupo: String
)
