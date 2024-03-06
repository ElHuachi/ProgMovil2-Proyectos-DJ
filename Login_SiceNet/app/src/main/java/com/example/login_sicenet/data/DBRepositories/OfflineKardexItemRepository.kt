package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.CargaAcademicaDAO
import com.example.login_sicenet.data.KardexItemDAO
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItemDB
import kotlinx.coroutines.flow.Flow

class OfflineKardexItemRepository(private val kardexDAO: KardexItemDAO): KardexItemRepository {
    override fun getAllItemsStream(matricula: String): List<KardexItemDB> = kardexDAO.getAllItems(matricula)

    override fun getItemStream(nControl: String): Flow<KardexItemDB?> = kardexDAO.getItem(nControl)

    override suspend fun insertItem(item: KardexItemDB) = kardexDAO.insert(item)

    override suspend fun deleteItem(item: KardexItemDB) = kardexDAO.delete(item)

    override suspend fun updateItem(item: KardexItemDB) = kardexDAO.update(item)

    override suspend fun updateQuery(matricula: String, fecha: String) = kardexDAO.updateQuery(matricula,fecha)


    override suspend fun insertItemAndGetId(item: KardexItemDB): Long {
        return kardexDAO.insertAndGetId(item)
    }
}