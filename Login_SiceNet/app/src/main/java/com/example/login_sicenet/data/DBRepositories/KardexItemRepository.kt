package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItemDB
import kotlinx.coroutines.flow.Flow

interface KardexItemRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<KardexItemDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<KardexItemDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: KardexItemDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: KardexItemDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: KardexItemDB)

    suspend fun insertItemAndGetId(item: KardexItemDB): Long
}