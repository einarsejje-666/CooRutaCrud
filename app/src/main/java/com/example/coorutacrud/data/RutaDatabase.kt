package com.example.coorutacrud.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ruta::class], version = 1)
abstract class RutaDatabase : RoomDatabase() {
    abstract fun rutaDao(): RutaDao

    companion object {
        @Volatile
        private var INSTANCE: RutaDatabase? = null

        // Una sola instancia de la base de datos para toda la app
        fun getDatabase(context: Context): RutaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RutaDatabase::class.java,
                    "cooruta_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
