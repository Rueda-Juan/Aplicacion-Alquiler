package com.Juan.myapplication.Data.Model

import java.time.LocalDate

data class CalendarEvent(
        val persona: Persona,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val title: String = persona.nombre,
        val type: EventType
)
