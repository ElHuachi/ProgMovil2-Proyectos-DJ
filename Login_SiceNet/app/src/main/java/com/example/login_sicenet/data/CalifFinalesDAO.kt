package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CalificacionDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CalifFinalesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cal: CalificacionDB)

    @Insert
    suspend fun insertAndGetId(cal: CalificacionDB): Long

    @Update
    suspend fun update(cal: CalificacionDB)

    @Delete
    suspend fun delete(cal: CalificacionDB)

    @Query("SELECT * from calificacionesFinales WHERE nControl = :nControl")
    fun getItem(nControl: String): Flow<CalificacionDB>

    @Query("SELECT * from calificacionesFinales ORDER BY nControl ASC")
    fun getAllItems(): Flow<List<CalificacionDB>>
}