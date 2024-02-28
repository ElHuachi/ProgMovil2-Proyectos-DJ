package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidadDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CalifPorUnidadDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cal: CalificacionUnidadDB)

    @Insert
    suspend fun insertAndGetId(cal: CalificacionUnidadDB): Long

    @Update
    suspend fun update(cal: CalificacionUnidadDB)

    @Delete
    suspend fun delete(cal: CalificacionUnidadDB)

    @Query("SELECT * from calificacionesUnidad WHERE nControl = :nControl")
    fun getItem(nControl: String): Flow<CalificacionUnidadDB>

    @Query("SELECT * from calificacionesUnidad ORDER BY nControl ASC")
    fun getAllItems(): Flow<List<CalificacionUnidadDB>>
}