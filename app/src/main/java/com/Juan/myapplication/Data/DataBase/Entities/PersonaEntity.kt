package com.Juan.myapplication.Data.DataBase.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Persona_tabla")
data class PersonaEntity(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Id") val id: Int = 0,
        @ColumnInfo(name = "Nombre") val nombre: String,
        @ColumnInfo(name = "Telefono") val telefono: Long,
        @ColumnInfo(name = "Dia") val diaInicial: String,
        @ColumnInfo(name = "Pagado") var cantPagado: Int,
        @ColumnInfo(name = "PrecioPorDia") var precioPorDia: Int,
        @ColumnInfo(name = "CantidadDias") val cantDias: Int
) {}
