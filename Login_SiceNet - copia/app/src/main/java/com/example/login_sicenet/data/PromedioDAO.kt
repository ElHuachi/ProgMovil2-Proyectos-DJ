package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.PromedioDB
import kotlinx.coroutines.flow.Flow

@Dao
interface PromedioDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: PromedioDB)

    @Insert
    suspend fun insertAndGetId(item: PromedioDB): Long

    @Update
    suspend fun update(item: PromedioDB)

    @Delete
    suspend fun delete(item: PromedioDB)

    @Query("SELECT * from promedio WHERE matricula = :matricula")
    fun getItem(matricula: String): Flow<PromedioDB>

    @Query("SELECT * from promedio ORDER BY matricula ASC")
    fun getAllItems(): Flow<List<PromedioDB>>
}