package com.Juan.myapplication.Data.Mapper

import java.time.LocalDate

class Converter {
  fun deStringALocalDate(date: String): LocalDate? {
    return try {
      LocalDate.parse(date)
    } catch (e: Exception) {
      null
    }
  }

  fun deLocalDateAString(date: LocalDate): String? {
    return try {
      date.toString()
    } catch (e: Exception) {
      null
    }
  }
}
