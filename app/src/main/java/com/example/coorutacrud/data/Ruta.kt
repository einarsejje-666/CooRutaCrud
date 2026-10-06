package com.example.coorutacrud.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad: una ruta que el conductor publica (casa -> trabajo)
@Entity(tableName = "rutas")
data class Ruta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "origen") val origen: String,
    @ColumnInfo(name = "destino") val destino: String,
    @ColumnInfo(name = "hora_salida") val horaSalida: String,
    @ColumnInfo(name = "asientos") val asientos: Int,
    @ColumnInfo(name = "precio") val precio: Double
)
