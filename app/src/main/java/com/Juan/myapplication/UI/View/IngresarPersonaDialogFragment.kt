package com.Juan.myapplication.UI.View

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.Juan.myapplication.Data.Model.Alquiler
import com.Juan.myapplication.Data.Model.Persona
import com.Juan.myapplication.R
import com.Juan.myapplication.UI.ViewModel.MainViewModel
import com.Juan.myapplication.databinding.FragmentIngresarPersonaBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

interface OnPersonaGuardadaListener {
  fun onPersonaGuardada()
}

@AndroidEntryPoint
class IngresarPersonaDialogFragment : DialogFragment() {
  private var _binding: FragmentIngresarPersonaBinding? = null
  private val binding
    get() = _binding!!
  private val viewModel: MainViewModel by viewModels()
  private var fechaSeleccionada: LocalDate = LocalDate.now()
  var listener: OnPersonaGuardadaListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.DialogTheme)
  }

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    _binding = FragmentIngresarPersonaBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupFechaSelector()
    setupGuardarButton()
  }

  private fun setupFechaSelector() {
    binding.fechaInicialEditText.setOnClickListener {
      val datePickerDialog =
              DatePickerDialog(
                      requireContext(),
                      { _, year, month, day ->
                        fechaSeleccionada = LocalDate.of(year, month + 1, day)
                        val fechaFormateada =
                                fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        binding.fechaInicialEditText.setText(fechaFormateada)
                      },
                      fechaSeleccionada.year,
                      fechaSeleccionada.monthValue - 1,
                      fechaSeleccionada.dayOfMonth
              )
      datePickerDialog.show()
    }
  }

  private fun setupGuardarButton() {
    binding.guardarButton.setOnClickListener {
      if (validarCampos()) {
        guardarPersona()
        Log.d("IngresarPersonaDialog", "Persona guardada, llamando a listener")
        listener?.onPersonaGuardada()
        dismiss()
      }
    }
  }

  private fun validarCampos(): Boolean {
    val nombre = binding.nombreEditText.text.toString()
    val telefono = binding.telefonoEditText.text.toString()
    val cantDias = binding.cantDiasEditText.text.toString()
    val precioPorDia = binding.precioPorDiaEditText.text.toString()
    val montoPagado = binding.montoPagadoEditText.text.toString()

    if (nombre.isEmpty() ||
                    telefono.isEmpty() ||
                    cantDias.isEmpty() ||
                    precioPorDia.isEmpty() ||
                    montoPagado.isEmpty()
    ) {
      mostrarError("Todos los campos son obligatorios")
      return false
    }
    return true
  }

  private fun guardarPersona() {
    try {
      val alquiler =
              Alquiler(
                      precioPorDia = binding.precioPorDiaEditText.text.toString().toInt(),
                      cantDias = binding.cantDiasEditText.text.toString().toInt()
              )

      val persona =
              Persona(
                      nombre = binding.nombreEditText.text.toString(),
                      telefono = binding.telefonoEditText.text.toString().toLong(),
                      diaInicial = fechaSeleccionada,
                      cantPagado = binding.montoPagadoEditText.text.toString().toInt(),
                      alquiler = alquiler
              )

      lifecycleScope.launch {
        try {
          viewModel.guardarPersona(persona)
          listener?.onPersonaGuardada()
          Log.d("MainViewModel", "Persona guardada con éxito: $persona")
          dismiss()
        } catch (e: Exception) {
          Log.e("MainViewModel", "Error al guardar persona: ${e.message}")
          requireActivity().runOnUiThread { mostrarError("Error al guardar: ${e.message}") }
        }
      }
    } catch (e: Exception) {
      mostrarError("Error en los datos: ${e.message}")
    }
  }

  private fun mostrarError(mensaje: String) {
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
  }

  private fun mostrarMensaje(mensaje: String) {
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    const val TAG = "IngresarPersonaDialog"

    fun newInstance(
        listener: OnPersonaGuardadaListener? = null,
        persona: Persona? = null
    ): IngresarPersonaDialogFragment {
        val fragment = IngresarPersonaDialogFragment()
        fragment.listener = listener
        persona?.let { 
            fragment.arguments = Bundle().apply { 
                putParcelable("persona", it) 
            } 
        }
        return fragment
    }
  }

  private fun cargarDatosPersona(persona: Persona) {
    binding.apply {
      nombreEditText.setText(persona.nombre)
      telefonoEditText.setText(persona.telefono.toString())
      fechaInicialEditText.setText(
              persona.diaInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
      )
      cantDiasEditText.setText(persona.alquiler.cantDias.toString())
      precioPorDiaEditText.setText(persona.alquiler.precioPorDia.toString())
      montoPagadoEditText.setText(persona.cantPagado.toString())
    }
  }
}
