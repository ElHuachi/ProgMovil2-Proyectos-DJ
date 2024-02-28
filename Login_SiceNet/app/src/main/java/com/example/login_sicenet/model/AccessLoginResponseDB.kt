package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "login")
data class AccessLoginResponseDB (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "nControl") var nControl: String,

    @ColumnInfo(name = "acceso")
    var acceso: Boolean? = null,

    @ColumnInfo(name = "estatus")
    var estatus: String? = null,

    @ColumnInfo(name = "tipo_usuario")
    var tipoUsuario: Int? = null,

    @ColumnInfo(name = "contrasenia")
    var contrasenia: String? = null,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null
)