package com.Juan.myapplication.Data

import com.Juan.myapplication.Data.DataBase.Dao.PersonaDao
import com.Juan.myapplication.Data.DataBase.Entities.PersonaEntity
import javax.inject.Inject

class Repository @Inject constructor(private val personaDao: PersonaDao) {
    suspend fun obtenerPersonasProximas(): List<PersonaEntity> {
        return personaDao.obtenerPersonasProximas()
    }
    suspend fun obtenerTodasLasPersonas(): List<PersonaEntity> {
        return personaDao.obtenerTodasLasPersonas()
    }

    suspend fun insertarPersona(persona: PersonaEntity) {
        return personaDao.insertarPersona(persona)
    }

    suspend fun eliminarPersona(id: Int) {
        return personaDao.eliminarPersona(id)
    }
    suspend fun actualizarPersona(persona: PersonaEntity){
        return personaDao.actualizarPersona(persona)
    }
}
