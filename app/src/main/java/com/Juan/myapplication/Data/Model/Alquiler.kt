package com.Juan.myapplication.Data.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Alquiler(var precioPorDia: Int, val cantDias: Int) : Parcelable {
    fun precioTotal(): Int {
        return this.precioPorDia * cantDias
    }

    fun modificarPrecioPorDia(nuevoPrecio: Int) {
        this.precioPorDia = nuevoPrecio
    }

    fun modificarTotalAPagar(total: Int) {
        this.precioPorDia = total / cantDias
    }
}
