package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItemDB
import kotlinx.coroutines.flow.Flow

@Dao
interface KardexItemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: KardexItemDB)

    @Insert
    suspend fun insertAndGetId(item: KardexItemDB): Long

    @Update
    suspend fun update(item: KardexItemDB)

    @Delete
    suspend fun delete(item: KardexItemDB)

    @Query("SELECT * from kardex_items WHERE matricula = :matricula")
    fun getItem(matricula: String): Flow<KardexItemDB>

    @Query("UPDATE kardex_items SET fecha = :fecha WHERE matricula = :matricula")
    fun updateQuery(matricula: String, fecha: String)

    @Query("SELECT * from kardex_items ORDER BY matricula ASC")
    fun getAllItems(): Flow<List<KardexItemDB>>
}