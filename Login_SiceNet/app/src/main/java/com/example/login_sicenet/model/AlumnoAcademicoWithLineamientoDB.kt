package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile",
    foreignKeys = [ForeignKey(
        entity = AccessLoginResponseDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["nControl"], unique = true)]
)
data class AlumnoAcademicoResultDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,

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
    val matricula: String? = null,

    @ColumnInfo(name = "fecha")
    val fecha: String? = "",
)

//PROFILE DB
data class ProfileUiState(
    val profileDetails: ProfileDetails = ProfileDetails(),
    val isEntryValid: Boolean = false
)

data class ProfileDetails(
    val id: Int = 0,
    val fechaReins: String? = null,
    val modEducativo: Int? = null,
    val adeudo: Boolean? = null,
    val urlFoto: String? = null,
    val adeudoDescripcion: String? = null,
    val inscrito: Boolean? = null,
    val estatus: String? = null,
    val semActual: Int? = null,
    val cdtosAcumulados: Int? = null,
    val cdtosActuales: Int? = null,
    val especialidad: String? = null,
    val carrera: String? = null,
    val lineamiento: Int? = null,
    val nombre: String? = null,
    val matricula: String? = null,
    val fecha: String? = ""
)


fun ProfileDetails.toItem(): AlumnoAcademicoResultDB = AlumnoAcademicoResultDB(
    id = id,
    matricula = matricula,
    fechaReins = fechaReins,
    estatus = estatus,
    modEducativo = modEducativo,
    adeudo = adeudo,
    urlFoto = urlFoto,
    adeudoDescripcion=adeudoDescripcion,
    inscrito=inscrito,
    semActual=semActual,
    cdtosAcumulados=cdtosAcumulados,
    cdtosActuales=cdtosActuales,
    especialidad=especialidad,
    carrera=carrera,
    lineamiento=lineamiento,
    nombre=nombre,
    fecha=fecha
)

fun AlumnoAcademicoResultDB.toItemUiState(isEntryValid: Boolean = false): ProfileUiState = ProfileUiState(
    profileDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun AlumnoAcademicoResultDB.toItemDetails(): ProfileDetails = ProfileDetails(
    id = id,
    matricula = matricula,
    fechaReins = fechaReins,
    estatus = estatus,
    modEducativo = modEducativo,
    adeudo = adeudo,
    urlFoto = urlFoto,
    adeudoDescripcion=adeudoDescripcion,
    inscrito=inscrito,
    semActual=semActual,
    cdtosAcumulados=cdtosAcumulados,
    cdtosActuales=cdtosActuales,
    especialidad=especialidad,
    carrera=carrera,
    lineamiento=lineamiento,
    nombre=nombre,
    fecha=fecha
)