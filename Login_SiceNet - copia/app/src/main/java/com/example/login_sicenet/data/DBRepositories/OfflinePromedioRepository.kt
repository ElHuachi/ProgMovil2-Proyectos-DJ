package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.KardexItemDAO
import com.example.login_sicenet.data.PromedioDAO
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.PromedioDB
import kotlinx.coroutines.flow.Flow

class OfflinePromedioRepository(private val promedioDAO: PromedioDAO): PromedioRepository {
    override fun getAllItemsStream(): Flow<List<PromedioDB>> = promedioDAO.getAllItems()

    override fun getItemStream(nControl: String): Flow<PromedioDB?> = promedioDAO.getItem(nControl)

    override suspend fun insertItem(item: PromedioDB) = promedioDAO.insert(item)

    override suspend fun deleteItem(item: PromedioDB) = promedioDAO.delete(item)

    override suspend fun updateItem(item: PromedioDB) = promedioDAO.update(item)

    override suspend fun insertItemAndGetId(item: PromedioDB): Long {
        return promedioDAO.insertAndGetId(item)
    }
}