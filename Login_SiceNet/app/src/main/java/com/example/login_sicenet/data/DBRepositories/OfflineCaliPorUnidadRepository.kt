package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.CalifFinalesDAO
import com.example.login_sicenet.data.CalifPorUnidadDAO
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidadDB
import kotlinx.coroutines.flow.Flow

class OfflineCaliPorUnidadRepository(private val caliUnidadDAO: CalifPorUnidadDAO): CaliPorUnidadRepository {
    override fun getAllItemsStream(nControl: String): Flow<List<CalificacionUnidadDB>> = caliUnidadDAO.getAllItems(nControl)

    override fun getItemStream(nControl: String): Flow<CalificacionUnidadDB?> = caliUnidadDAO.getItem(nControl)

    override suspend fun insertItem(item: CalificacionUnidadDB) = caliUnidadDAO.insert(item)

    override suspend fun deleteItem(item: CalificacionUnidadDB) = caliUnidadDAO.delete(item)

    override suspend fun updateItem(item: CalificacionUnidadDB) = caliUnidadDAO.update(item)

    override suspend fun updateQuery(matricula: String, fecha: String) = caliUnidadDAO.updateQuery(matricula,fecha)

    override suspend fun insertItemAndGetId(item: CalificacionUnidadDB): Long {
        return caliUnidadDAO.insertAndGetId(item)
    }
}