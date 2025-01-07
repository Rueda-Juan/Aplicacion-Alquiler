package com.Juan.myapplication.Data.DataBase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.Juan.myapplication.Data.DataBase.Entities.PersonaEntity

@Dao
interface PersonaDao {
    @Query("SELECT * FROM Persona_tabla ORDER BY Id DESC")
    fun obtenerTodasLasPersonas(): List<PersonaEntity>

    @Query("SELECT * FROM Persona_tabla WHERE Dia >= CURRENT_DATE ORDER BY Dia ASC")
    fun obtenerPersonasProximas(): List<PersonaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(personas: List<PersonaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPersona(persona: PersonaEntity)

    @Update fun actualizarPersona(persona: PersonaEntity)

    @Query("DELETE FROM Persona_tabla WHERE Id = :id") suspend fun eliminarPersona(id: Int)
}
