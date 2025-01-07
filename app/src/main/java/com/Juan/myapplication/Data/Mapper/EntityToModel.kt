package com.Juan.myapplication.Data.Mapper

import com.Juan.myapplication.Data.DataBase.Entities.PersonaEntity
import com.Juan.myapplication.Data.Model.Alquiler
import com.Juan.myapplication.Data.Model.Persona
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object EntityToModel {
  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

  fun toPersona(personaEntity: PersonaEntity): Persona {
    return Persona(
            nombre = personaEntity.nombre,
            telefono = personaEntity.telefono,
            diaInicial = LocalDate.parse(personaEntity.diaInicial, formatter),
            cantPagado = personaEntity.cantPagado,
            alquiler = Alquiler(
                    precioPorDia = personaEntity.precioPorDia,
                    cantDias = personaEntity.cantDias
            ),
            id = personaEntity.id
    )
  }

  fun toPersonaEntity(persona: Persona): PersonaEntity {
    return PersonaEntity(
            id = persona.id,
            nombre = persona.nombre,
            telefono = persona.telefono,
            diaInicial = persona.diaInicial.format(formatter),
            cantPagado = persona.cantPagado,
            precioPorDia = persona.alquiler.precioPorDia,
            cantDias = persona.alquiler.cantDias
    )
  }
}
