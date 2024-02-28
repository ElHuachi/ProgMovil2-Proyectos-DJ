package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile",
    foreignKeys = [ForeignKey(
        entity = AccessLoginResponseDB::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class AlumnoAcademicoResultDB(
    @PrimaryKey
    @ColumnInfo(name = "nControl")
    val nControl: String = "",

    @ColumnInfo(name = "fechaReins")
    val fechaReins: String? = null,

    @ColumnInfo(name = "modEducativo")
    val modEducativo: Int? = null,

    @ColumnInfo(name = "adeudo")
    val adeudo: Boolean? = null,

    @ColumnInfo(name = "urlFoto")
    val urlFoto: String? = null,

    @ColumnInfo(name = "adeudoDescripcion")
    val adeudoDescripcion: String? = null,

    @ColumnInfo(name = "inscrito")
    val inscrito: Boolean? = null,

    @ColumnInfo(name = "estatus")
    val estatus: String? = null,

    @ColumnInfo(name = "semActual")
    val semActual: Int? = null,

    @ColumnInfo(name = "cdtosAcumulados")
    val cdtosAcumulados: Int? = null,

    @ColumnInfo(name = "cdtosActuales")
    val cdtosActuales: Int? = null,

    @ColumnInfo(name = "especialidad")
    val especialidad: String? = null,

    @ColumnInfo(name = "carrera")
    val carrera: String? = null,

    @ColumnInfo(name = "lineamiento")
    val lineamiento: Int? = null,

    @ColumnInfo(name = "nombre")
    val nombre: String? = null,

    @ColumnInfo(name = "matricula")
    val matricula: String? = null
)