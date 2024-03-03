package com.example.login_sicenet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "kardex_items",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class KardexItemDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int=0,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "s3")
    val s3: String?,

    @ColumnInfo(name = "p3")
    val p3: String?,

    @ColumnInfo(name = "a3")
    val a3: String?,

    @ColumnInfo(name = "clvMat")
    val clvMat: String?,

    @ColumnInfo(name = "clvOfiMat")
    val clvOfiMat: String?,

    @ColumnInfo(name = "materia")
    val materia: String?,

    @ColumnInfo(name = "cdts")
    val cdts: Int?,

    @ColumnInfo(name = "calif")
    val calif: Int?,

    @ColumnInfo(name = "acred")
    val acred: String?,

    @ColumnInfo(name = "s1")
    val s1: String?,

    @ColumnInfo(name = "p1")
    val p1: String?,

    @ColumnInfo(name = "a1")
    val a1: String?,

    @ColumnInfo(name = "s2")
    val s2: String?,

    @ColumnInfo(name = "p2")
    val p2: String?,

    @ColumnInfo(name = "a2")
    val a2: String?,

    @ColumnInfo(name = "fecha")
    val fecha: String
)

//KARDEX DB
data class KardexUiState(
    val kardexItemDetails: KardexItemDetails = KardexItemDetails(),
    val isEntryValid: Boolean = true
)

data class KardexItemDetails(
    val id: Int = 0,
    val matricula: String? = null,
    val s3: String? = "",
    val p3: String? = "",
    val a3: String? = "",
    val clvMat: String? = "",
    val clvOfiMat: String? = "",
    val cdts: Int? = 0,
    val calif: Int? = 0,
    val acred: String? = "",
    val s1: String? = "",
    val p1: String? = "",
    val a1: String?="",
    val s2: String? = "",
    val p2: String? = "",
    val a2: String?="",
    val materia: String?="",
    val fecha: String = "",
)


fun KardexItemDetails.toItem(): KardexItemDB = KardexItemDB(
    id = id,
    matricula = matricula,
    s3 = s3,
    p3 = p3,
    a3 = a3,
    s2 = s2,
    p2 = p2,
    a2 = a2,
    s1 = s1,
    p1 = p1,
    a1 = a1,
    materia = materia,
    clvMat = clvMat,
    clvOfiMat = clvOfiMat,
    cdts = cdts,
    calif = calif,
    acred = acred,
    fecha=fecha
)

fun KardexItemDB.toItemUiState(isEntryValid: Boolean = false): KardexUiState = KardexUiState(
    kardexItemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun KardexItemDB.toItemDetails(): KardexItemDetails = KardexItemDetails(
    id = id,
    matricula = matricula,
    s3 = s3,
    p3 = p3,
    a3 = a3,
    s2 = s2,
    p2 = p2,
    a2 = a2,
    s1 = s1,
    p1 = p1,
    a1 = a1,
    materia = materia,
    clvMat = clvMat,
    clvOfiMat = clvOfiMat,
    cdts = cdts,
    calif = calif,
    acred = acred,
    fecha=fecha
)


//PROMEDIO
@Entity(tableName = "promedio",
    foreignKeys = [ForeignKey(
        entity = AlumnoAcademicoResultDB::class,
        parentColumns = ["matricula"],
        childColumns = ["matricula"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["matricula"], unique = true)]
)
data class PromedioDB(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int=0,

    @ColumnInfo(name = "matricula")
    var matricula: String? = null,

    @ColumnInfo(name = "promedioGral")
    val promedioGral: Double?,

    @ColumnInfo(name = "cdtsAcum")
    val cdtsAcum: Int?,

    @ColumnInfo(name = "cdtsPlan")
    val cdtsPlan: Int?,

    @ColumnInfo(name = "matCursadas")
    val matCursadas: Int?,

    @ColumnInfo(name = "matAprobadas")
    val matAprobadas: Int?,

    @ColumnInfo(name = "avanceCdts")
    val avanceCdts: Double?,

    @ColumnInfo(name = "fecha")
    val fecha: String
)

//PROMEDIO DB
data class PromedioUiState(
    val promedioDetails: PromedioDetails = PromedioDetails(),
    val isEntryValid: Boolean = true
)

data class PromedioDetails(
    val id: Int = 0,
    val matricula: String? = null,
    val promedioGral: Double? = 0.0,
    val cdtsAcum: Int? = 0,
    val cdtsPlan: Int? = 0,
    val matCursadas: Int? = 0,
    val matAprobadas: Int? = 0,
    val avanceCdts: Double? = 0.0,
    val fecha: String = "",
)


fun PromedioDetails.toItem(): PromedioDB = PromedioDB(
    id = id,
    matricula = matricula,
    promedioGral = promedioGral,
    cdtsAcum = cdtsAcum,
    cdtsPlan = cdtsPlan,
    matCursadas = matCursadas,
    matAprobadas = matAprobadas,
    avanceCdts = avanceCdts,
    fecha=fecha
)

fun PromedioDB.toItemUiState(isEntryValid: Boolean = false): PromedioUiState = PromedioUiState(
    promedioDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun PromedioDB.toItemDetails(): PromedioDetails = PromedioDetails(
    id = id,
    matricula = matricula,
    promedioGral = promedioGral,
    cdtsAcum = cdtsAcum,
    cdtsPlan = cdtsPlan,
    matCursadas = matCursadas,
    matAprobadas = matAprobadas,
    avanceCdts = avanceCdts,
    fecha=fecha
)
