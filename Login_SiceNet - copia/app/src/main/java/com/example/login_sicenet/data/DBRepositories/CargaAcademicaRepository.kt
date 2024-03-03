package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.CargaAcademicaItemDB
import kotlinx.coroutines.flow.Flow

interface CargaAcademicaRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<CargaAcademicaItemDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<CargaAcademicaItemDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: CargaAcademicaItemDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: CargaAcademicaItemDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: CargaAcademicaItemDB)

    suspend fun insertItemAndGetId(item: CargaAcademicaItemDB): Long
}