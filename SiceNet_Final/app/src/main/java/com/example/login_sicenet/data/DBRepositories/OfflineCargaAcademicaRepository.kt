package com.example.login_sicenet.data.DBRepositories

import com.example.login_sicenet.data.CalifPorUnidadDAO
import com.example.login_sicenet.data.CargaAcademicaDAO
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.CargaAcademicaItemDB
import kotlinx.coroutines.flow.Flow

class OfflineCargaAcademicaRepository (private val cargaAcDAO: CargaAcademicaDAO): CargaAcademicaRepository{
    override fun getAllItemsStream(matricula: String): List<CargaAcademicaItemDB> = cargaAcDAO.getAllItems(matricula)

    override fun getItemStream(nControl: String): Flow<CargaAcademicaItemDB?> = cargaAcDAO.getItem(nControl)

    override suspend fun insertItem(item: CargaAcademicaItemDB) = cargaAcDAO.insert(item)

    override suspend fun deleteItem(item: CargaAcademicaItemDB) = cargaAcDAO.delete(item)

    override suspend fun updateItem(item: CargaAcademicaItemDB) = cargaAcDAO.update(item)

    override suspend fun updateQuery(matricula: String, fecha: String) = cargaAcDAO.updateQuery(matricula,fecha)

    override suspend fun insertItemAndGetId(item: CargaAcademicaItemDB): Long {
        return cargaAcDAO.insertAndGetId(item)
    }
}