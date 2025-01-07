package com.Juan.myapplication

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.Juan.myapplication.Data.DataBase.AppDataBase
import com.Juan.myapplication.Data.DataBase.Dao.PersonaDao
import com.Juan.myapplication.Data.DataBase.Entities.PersonaEntity
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class DataBaseTest {
  @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule val mainCoroutineRule = MainCoroutineRule()

  private lateinit var db: AppDataBase
  private lateinit var personaDao: PersonaDao

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
    personaDao = db.getPersonaDao()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun insertAndGetPersona() = runTest {
    val persona =
            PersonaEntity(
                    nombre = "Juan",
                    telefono = 1234567890,
                    diaInicial = LocalDate.now().toString(),
                    cantPagado = 1000,
                    precioPorDia = 500,
                    cantDias = 5
            )
    personaDao.insertarPersona(persona)

    val personas = personaDao.obtenerTodasLasPersonas()
    assertEquals(1, personas.size)
    assertEquals("Juan", personas.first().nombre)
    assertEquals(500, personas.first().precioPorDia)
  }

  @Test
  fun getPersonasProximas() = runTest {
    val fechaHoy = LocalDate.now()
    val fechaFutura = fechaHoy.plusDays(5)
    val fechaPasada = fechaHoy.minusDays(5)

    val personaFutura =
            PersonaEntity(
                    nombre = "Juan",
                    telefono = 1234567890,
                    diaInicial = fechaFutura.toString(),
                    cantPagado = 2000,
                    precioPorDia = 1000,
                    cantDias = 5
            )
    val personaPasada =
            PersonaEntity(
                    nombre = "Ana",
                    telefono = 987654321,
                    diaInicial = fechaPasada.toString(),
                    cantPagado = 1500,
                    precioPorDia = 1000,
                    cantDias = 5
            )

    personaDao.insertarPersona(personaFutura)
    personaDao.insertarPersona(personaPasada)

    val personasProximas = personaDao.obtenerPersonasProximas()
    assertEquals(1, personasProximas.size)
    assertEquals("Juan", personasProximas.first().nombre)
  }
}
