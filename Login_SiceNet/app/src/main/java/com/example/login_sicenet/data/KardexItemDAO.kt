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

    @Query("SELECT * from kardex_items WHERE nControl = :nControl")
    fun getItem(nControl: String): Flow<KardexItemDB>

    @Query("SELECT * from kardex_items ORDER BY nControl ASC")
    fun getAllItems(): Flow<List<KardexItemDB>>
}