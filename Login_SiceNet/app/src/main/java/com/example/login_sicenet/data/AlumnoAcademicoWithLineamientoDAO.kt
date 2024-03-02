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

    @Query("UPDATE profile SET fecha=:fecha WHERE matricula = :matricula")
    suspend fun updateQuery(matricula: String, fecha: String)

    @Delete
    suspend fun delete(profile: AlumnoAcademicoResultDB)

    @Query("SELECT * from profile WHERE matricula = :matricula")
    fun getItem(matricula: String): Flow<AlumnoAcademicoResultDB>

    @Query("SELECT * from profile ORDER BY matricula ASC")
    fun getAllItems(): Flow<List<AlumnoAcademicoResultDB>>
}