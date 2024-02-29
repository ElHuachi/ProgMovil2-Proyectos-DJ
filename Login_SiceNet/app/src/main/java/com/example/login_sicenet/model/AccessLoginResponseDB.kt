package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "login", indices = [Index(value = ["matricula"], unique = true)])
data class AccessLoginResponseDB (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,

    @ColumnInfo(name = "acceso")
    var acceso: Boolean? = null,

    @ColumnInfo(name = "estatus")
    var estatus: String? = null,

    @ColumnInfo(name = "tipo_usuario")
    var tipoUsuario: Int? = null,

    @ColumnInfo(name = "contrasenia")
    var contrasenia: String? = null,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "fecha")
    val fecha: String,
)

//ACCESO DB
data class AccesoUiState(
    val accesoDetails: AccesoDetails = AccesoDetails(),
    val isEntryValid: Boolean = false
)

data class AccesoDetails(
    val id: Int = 0,
    val acceso: Boolean? = null,
    val estatus: String? = null,
    val tipo_usuario: Int? = null,
    val contrasenia: String? = null,
    val matricula: String? = null,
    val fecha: String = ""
)

fun AccesoDetails.toItem(): AccessLoginResponseDB = AccessLoginResponseDB(
    id = id,
    matricula = matricula,
    acceso = acceso,
    estatus = estatus,
    tipoUsuario = tipo_usuario,
    contrasenia = contrasenia,
    fecha = fecha
)

fun AccessLoginResponseDB.toItemUiState(isEntryValid: Boolean = false): AccesoUiState = AccesoUiState(
    accesoDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun AccessLoginResponseDB.toItemDetails(): AccesoDetails = AccesoDetails(
    id = id,
    matricula = matricula,
    acceso = acceso,
    estatus = estatus,
    tipo_usuario  = tipoUsuario,
    contrasenia = contrasenia,
    fecha = fecha
)