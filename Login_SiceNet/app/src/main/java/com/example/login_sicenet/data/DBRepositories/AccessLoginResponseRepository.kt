package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.model.AccessLoginResponseDB
import kotlinx.coroutines.flow.Flow

interface AccessLoginResponseRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<AccessLoginResponseDB>>

    /**
     * Retrieve an item from the given data source that matches with the [nControl].
     */
    fun getItemStream(nControl: String): Flow<AccessLoginResponseDB?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: AccessLoginResponseDB)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: AccessLoginResponseDB)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: AccessLoginResponseDB)

    suspend fun insertItemAndGetId(item: AccessLoginResponseDB): Long
}