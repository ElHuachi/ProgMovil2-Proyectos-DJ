package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.AccessLoginResponseDB
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessLoginResponseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(accessLogin: AccessLoginResponseDB)

    @Insert
    suspend fun insertAndGetId(login: AccessLoginResponseDB): Long

    @Update
    suspend fun update(login: AccessLoginResponseDB)

    @Query("UPDATE login SET fecha = :fecha WHERE matricula = :matricula")
    suspend fun updateQuery(matricula: String, fecha: String)

    @Delete
    suspend fun delete(login: AccessLoginResponseDB)

    @Query("SELECT * from login WHERE matricula = :matricula")
    fun getItem(matricula: String): Flow<AccessLoginResponseDB>

    @Query("SELECT * from login ORDER BY matricula ASC")
    fun getAllItems(): Flow<List<AccessLoginResponseDB>>
}