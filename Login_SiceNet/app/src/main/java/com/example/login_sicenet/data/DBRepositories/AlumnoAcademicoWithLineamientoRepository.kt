package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.AccessLoginResponseDB
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import kotlinx.coroutines.flow.Flow

interface AlumnoAcademicoWithLineamientoRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<AlumnoAcademicoResultDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<AlumnoAcademicoResultDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: AlumnoAcademicoResultDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: AlumnoAcademicoResultDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: AlumnoAcademicoResultDB)

    suspend fun insertItemAndGetId(item: AlumnoAcademicoResultDB): Long
}