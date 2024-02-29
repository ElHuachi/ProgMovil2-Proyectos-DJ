package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.CargaAcademicaItemDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CargaAcademicaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CargaAcademicaItemDB)

    @Insert
    suspend fun insertAndGetId(item: CargaAcademicaItemDB): Long

    @Update
    suspend fun update(item: CargaAcademicaItemDB)

    @Delete
    suspend fun delete(item: CargaAcademicaItemDB)

    @Query("SELECT * from carga_academica_items WHERE matricula = :matricula")
    fun getItem(matricula: String): Flow<CargaAcademicaItemDB>

    @Query("SELECT * from carga_academica_items ORDER BY matricula ASC")
    fun getAllItems(): Flow<List<CargaAcademicaItemDB>>
}