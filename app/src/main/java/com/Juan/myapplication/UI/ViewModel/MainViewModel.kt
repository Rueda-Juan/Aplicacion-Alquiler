package com.Juan.myapplication.UI.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Juan.myapplication.Data.Mapper.EntityToModel
import com.Juan.myapplication.Data.Model.CalendarEvent
import com.Juan.myapplication.Data.Model.EventType
import com.Juan.myapplication.Data.Model.Persona
import com.Juan.myapplication.Data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

  private val _alquileres = MutableLiveData<List<Persona>>()
  val alquileres: LiveData<List<Persona>> = _alquileres

  private val _error = MutableLiveData<String>()
  val error: LiveData<String> = _error

  private val _events = MutableLiveData<List<CalendarEvent>>()
  val events: LiveData<List<CalendarEvent>> = _events

  private val _mensaje = MutableLiveData<String>()
  val mensaje: LiveData<String> = _mensaje

  fun cargarAlquileres() {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val personas = repository.obtenerTodasLasPersonas()
        Log.d("MainViewModel", "Personas cargadas: ${personas.size}")
        val personasConAlquileres =
                personas.map { personaEntity ->
                  EntityToModel.toPersona(personaEntity).also {
                    Log.d("MainViewModel", "Persona convertida: ${it.nombre}")
                  }
                }
        withContext(Dispatchers.Main) { _alquileres.postValue(personasConAlquileres) }
      } catch (e: Exception) {
        Log.e("MainViewModel", "Error al cargar alquileres", e)
        withContext(Dispatchers.Main) {
          _error.postValue(e.message ?: "Error al cargar alquileres")
        }
      }
    }
  }

  fun guardarPersona(persona: Persona) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val personaEntity = EntityToModel.toPersonaEntity(persona)
        repository.insertarPersona(personaEntity)
        cargarAlquileres()
        cargarEventosCalendario()
        withContext(Dispatchers.Main) { _mensaje.value = "Persona guardada exitosamente" }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) {
          _error.value = e.message ?: "Error al guardar"
          Log.e("MainViewModel", "Error guardando persona", e)
        }
      }
    }
  }

  fun cargarEventosCalendario() {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val personas = repository.obtenerTodasLasPersonas()
        val events = mutableListOf<CalendarEvent>()

        personas.forEach { personaEntity ->
          val persona = EntityToModel.toPersona(personaEntity)
          // Verificar que la fecha no sea nula
          if (persona.diaInicial != null) {
            // Evento de entrada
            events.add(
                    CalendarEvent(
                            persona = persona,
                            startDate = persona.diaInicial,
                            endDate = persona.fechaDeSalida(),
                            type = EventType.ENTRADA,
                            title = "${persona.nombre}\n$${persona.alquiler.precioPorDia}/día"
                    )
            )
            // Evento de salida
            events.add(
                    CalendarEvent(
                            persona = persona,
                            startDate = persona.fechaDeSalida(),
                            endDate = persona.fechaDeSalida(),
                            type = EventType.SALIDA,
                            title = "${persona.nombre} - Salida"
                    )
            )
          }
        }

        withContext(Dispatchers.Main) {
          _events.postValue(events)
          Log.d("MainViewModel", "Eventos cargados: ${events.size}")
          events.forEach {
            Log.d(
                    "MainViewModel",
                    "Evento: ${it.title}, Inicio: ${it.startDate}, Fin: ${it.endDate}"
            )
          }
        }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) {
          _error.postValue("Error al cargar eventos: ${e.message}")
          Log.e("MainViewModel", "Error cargando eventos", e)
        }
      }
    }
  }

  fun eliminarPersona(persona: Persona) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repository.eliminarPersona(persona.id)
        cargarAlquileres()
        cargarEventosCalendario()
        withContext(Dispatchers.Main) {
          _mensaje.value = "Persona eliminada con éxito"
          _alquileres.value = _alquileres.value?.filter { it.id != persona.id }
        }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) { _error.value = "Error al eliminar: ${e.message}" }
      }
    }
  }

  fun actualizarPersona(persona: Persona) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        repository.actualizarPersona(EntityToModel.toPersonaEntity(persona))
        cargarAlquileres()
        cargarEventosCalendario()
        withContext(Dispatchers.Main) { _mensaje.value = "Pago actualizado con éxito" }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) { _error.value = "Error al actualizar: ${e.message}" }
      }
    }
  }
}
