package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CalificacionDB
import kotlinx.coroutines.flow.Flow

interface CaliFinalesRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<CalificacionDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<CalificacionDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: CalificacionDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: CalificacionDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: CalificacionDB)

    suspend fun insertItemAndGetId(item: CalificacionDB): Long
}