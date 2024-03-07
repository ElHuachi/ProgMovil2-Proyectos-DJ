package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.PromedioDB
import kotlinx.coroutines.flow.Flow

interface PromedioRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<PromedioDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<PromedioDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: PromedioDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: PromedioDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: PromedioDB)

    suspend fun updateQuery(matricula: String, fecha: String)

    suspend fun insertItemAndGetId(item: PromedioDB): Long
}