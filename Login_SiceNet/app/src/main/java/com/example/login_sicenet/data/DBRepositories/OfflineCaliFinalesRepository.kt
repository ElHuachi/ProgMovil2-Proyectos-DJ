package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.AlumnoAcademicoWithLineamientoDAO
import com.example.login_sicenet.data.CalifFinalesDAO
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CalificacionDB
import kotlinx.coroutines.flow.Flow

class OfflineCaliFinalesRepository(private val caliFinalesDAO: CalifFinalesDAO): CaliFinalesRepository {
    override fun getAllItemsStream(): Flow<List<CalificacionDB>> = caliFinalesDAO.getAllItems()

    override fun getItemStream(nControl: String): Flow<CalificacionDB?> = caliFinalesDAO.getItem(nControl)

    override suspend fun insertItem(item: CalificacionDB) = caliFinalesDAO.insert(item)

    override suspend fun deleteItem(item: CalificacionDB) = caliFinalesDAO.delete(item)

    override suspend fun updateItem(item: CalificacionDB) = caliFinalesDAO.update(item)

    override suspend fun insertItemAndGetId(item: CalificacionDB): Long {
        return caliFinalesDAO.insertAndGetId(item)
    }
}