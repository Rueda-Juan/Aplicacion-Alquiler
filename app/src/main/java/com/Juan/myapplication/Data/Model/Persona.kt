package com.Juan.myapplication.Data.Model

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Persona(
    val nombre: String,
    val telefono: Long,
    val diaInicial: LocalDate,
    var cantPagado: Int,
    val alquiler: Alquiler,
    val id: Int = 0
) : Parcelable {
    fun fechaDeSalida(): LocalDate {
        return diaInicial.plusDays(alquiler.cantDias.toLong())
    }

    fun faltaPagar(): Int {
        return alquiler.precioTotal() - cantPagado
    }
}
