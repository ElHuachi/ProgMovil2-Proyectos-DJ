package com.example.login_sicenet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.login_sicenet.model.AccessLoginResponseDB
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import kotlinx.coroutines.flow.Flow

@Dao
interface AlumnoAcademicoWithLineamientoDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: AlumnoAcademicoResultDB)

    @Insert
    suspend fun insertAndGetId(profile: AlumnoAcademicoResultDB): Long

    @Update
    suspend fun update(profile: AlumnoAcademicoResultDB)

    @Delete
    suspend fun delete(profile: AlumnoAcademicoResultDB)

    @Query("SELECT * from profile WHERE nControl = :nControl")
    fun getItem(nControl: String): Flow<AlumnoAcademicoResultDB>

    @Query("SELECT * from profile ORDER BY nControl ASC")
    fun getAllItems(): Flow<List<AlumnoAcademicoResultDB>>
}