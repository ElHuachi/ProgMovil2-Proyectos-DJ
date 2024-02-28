package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.AccessLoginResponseDAO
import com.example.login_sicenet.data.AlumnoAcademicoWithLineamientoDAO
import com.example.login_sicenet.model.AccessLoginResponseDB
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import kotlinx.coroutines.flow.Flow

class OfflineAlumnoAcademicoWithLineamientoRepository(private val profileDAO: AlumnoAcademicoWithLineamientoDAO):
    AlumnoAcademicoWithLineamientoRepository {
    override fun getAllItemsStream(): Flow<List<AlumnoAcademicoResultDB>> = profileDAO.getAllItems()

    override fun getItemStream(nControl: String): Flow<AlumnoAcademicoResultDB?> = profileDAO.getItem(nControl)

    override suspend fun insertItem(item: AlumnoAcademicoResultDB) = profileDAO.insert(item)

    override suspend fun deleteItem(item: AlumnoAcademicoResultDB) = profileDAO.delete(item)

    override suspend fun updateItem(item: AlumnoAcademicoResultDB) = profileDAO.update(item)

    override suspend fun insertItemAndGetId(item: AlumnoAcademicoResultDB): Long {
        return profileDAO.insertAndGetId(item)
    }
}