package com.example.login_sicenet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.login_sicenet.model.AccessLoginResponseDB
import com.example.login_sicenet.model.AlumnoAcademicoResultDB
import com.example.login_sicenet.model.CalificacionDB
import com.example.login_sicenet.model.CalificacionUnidadDB
import com.example.login_sicenet.model.CargaAcademicaItemDB
import com.example.login_sicenet.model.KardexItemDB
import com.example.login_sicenet.model.PromedioDB

@Database(
    entities = [
        AccessLoginResponseDB::class,
        AlumnoAcademicoResultDB::class,
        CalificacionUnidadDB::class,
        CalificacionDB::class,
        KardexItemDB::class,
        PromedioDB::class,
        CargaAcademicaItemDB::class],
    version = 1, exportSchema = false)
abstract class AlumnoDatabase: RoomDatabase(){

    abstract fun accessDao(): AccessLoginResponseDAO
    abstract fun profileDao(): AlumnoAcademicoWithLineamientoDAO
    abstract fun caliFinalesDao(): CalifFinalesDAO
    abstract fun caliPorUnidadDao(): CalifPorUnidadDAO
    abstract fun kardexDao(): KardexItemDAO
    abstract fun promedioDao(): PromedioDAO
    abstract fun cargaACDao(): CargaAcademicaDAO

    companion object {
        @Volatile
        private var Instance: AlumnoDatabase? = null

        fun getDatabase(context: Context): AlumnoDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AlumnoDatabase::class.java, "alumno_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}