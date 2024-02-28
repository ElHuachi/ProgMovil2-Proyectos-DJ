package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "carga_academica_items",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class CargaAcademicaItemDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "carga_academica_item_id")
    val cargaAcademicaItemId: Long = 0,

    @ColumnInfo(name = "nControl")
    val nControl: String = "",

    @ColumnInfo(name = "semipresencial")
    val semipresencial: String?,

    @ColumnInfo(name = "observaciones")
    val observaciones: String?,

    @ColumnInfo(name = "docente")
    val docente: String,

    @ColumnInfo(name = "clvOficial")
    val clvOficial: String,

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
    val estadoMateria: String,

    @ColumnInfo(name = "creditosMateria")
    val creditosMateria: Int,

    @ColumnInfo(name = "materia")
    val materia: String,

    @ColumnInfo(name = "grupo")
    val grupo: String
)
