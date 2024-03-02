package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.AccessLoginResponseDAO
import com.example.login_sicenet.model.AccessLoginResponseDB
import kotlinx.coroutines.flow.Flow

class OfflineAccessLoginResponseRepository(private val accessDAO: AccessLoginResponseDAO): AccessLoginResponseRepository {
    override fun getAllItemsStream(): Flow<List<AccessLoginResponseDB>> = accessDAO.getAllItems()

    override fun getItemStream(nControl: String): Flow<AccessLoginResponseDB?> = accessDAO.getItem(nControl)

    override suspend fun insertItem(item: AccessLoginResponseDB) = accessDAO.insert(item)

    override suspend fun deleteItem(item: AccessLoginResponseDB) = accessDAO.delete(item)

    override suspend fun updateItem(item: AccessLoginResponseDB) = accessDAO.update(item)

    override suspend fun updateItemQuery(matricula: String, fecha: String) = accessDAO.updateQuery(matricula, fecha)

    override suspend fun insertItemAndGetId(item: AccessLoginResponseDB): Long {
        return accessDAO.insertAndGetId(item)
    }
}