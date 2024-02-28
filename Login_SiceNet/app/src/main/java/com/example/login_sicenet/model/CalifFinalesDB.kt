package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificacionesFinales",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CalificacionDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "calificacion_id")
    val calificacionId: Long = 0,

    @ColumnInfo(name = "nControl")
    val nControl: String = "",

    @ColumnInfo(name = "calif")
    val calif: Int,

    @ColumnInfo(name = "acred")
    val acred: String,

    @ColumnInfo(name = "grupo")
    val grupo: String,

    @ColumnInfo(name = "materia")
    val materia: String,

    @ColumnInfo(name = "observaciones")
    val observaciones: String
)
