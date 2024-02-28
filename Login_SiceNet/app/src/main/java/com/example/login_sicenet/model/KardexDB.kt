package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "kardex_items",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResult::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class KardexItemDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "kardex_item_id")
    val kardexItemId: Long = 0,

    @ColumnInfo(name = "nControl")
    val nControl: String = "",

    @ColumnInfo(name = "s3")
    val s3: String?,

    @ColumnInfo(name = "p3")
    val p3: String?,

    @ColumnInfo(name = "a3")
    val a3: String?,

    @ColumnInfo(name = "clvMat")
    val clvMat: String,

    @ColumnInfo(name = "clvOfiMat")
    val clvOfiMat: String,

    @ColumnInfo(name = "materia")
    val materia: String,

    @ColumnInfo(name = "cdts")
    val cdts: Int,

    @ColumnInfo(name = "calif")
    val calif: Int,

    @ColumnInfo(name = "acred")
    val acred: String,

    @ColumnInfo(name = "s1")
    val s1: String,

    @ColumnInfo(name = "p1")
    val p1: String,

    @ColumnInfo(name = "a1")
    val a1: String,

    @ColumnInfo(name = "s2")
    val s2: String?,

    @ColumnInfo(name = "p2")
    val p2: String?,

    @ColumnInfo(name = "a2")
    val a2: String?
)

@Entity(tableName = "promedio",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["nControl"],
        childColumns = ["nControl"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PromedioDB(
    @PrimaryKey
    @ColumnInfo(name = "nControl")
    val nControl: String = "",

    @ColumnInfo(name = "promedioGral")
    val promedioGral: Double,

    @ColumnInfo(name = "cdtsAcum")
    val cdtsAcum: Int,

    @ColumnInfo(name = "cdtsPlan")
    val cdtsPlan: Int,

    @ColumnInfo(name = "matCursadas")
    val matCursadas: Int,

    @ColumnInfo(name = "matAprobadas")
    val matAprobadas: Int,

    @ColumnInfo(name = "avanceCdts")
    val avanceCdts: Double
)

