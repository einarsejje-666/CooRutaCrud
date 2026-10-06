package com.example.coorutacrud.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO con las 4 operaciones del CRUD
@Dao
interface RutaDao {
    // READ: devuelve un Flow, así la lista se actualiza sola
    @Query("SELECT * FROM rutas ORDER BY id DESC")
    fun getAll(): Flow<List<Ruta>>

    // CREATE
    @Insert
    suspend fun insert(ruta: Ruta)

    // UPDATE
    @Update
    suspend fun update(ruta: Ruta)

    // DELETE
    @Delete
    suspend fun delete(ruta: Ruta)
}
