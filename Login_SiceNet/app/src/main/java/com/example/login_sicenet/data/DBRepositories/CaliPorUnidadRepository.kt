package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidadDB
import kotlinx.coroutines.flow.Flow

interface CaliPorUnidadRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<CalificacionUnidadDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<CalificacionUnidadDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: CalificacionUnidadDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: CalificacionUnidadDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: CalificacionUnidadDB)

    suspend fun insertItemAndGetId(item: CalificacionUnidadDB): Long
}