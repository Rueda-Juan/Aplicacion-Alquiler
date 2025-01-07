package com.Juan.myapplication.UI.View

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.Juan.myapplication.Data.Model.Persona
import com.Juan.myapplication.UI.ViewModel.MainViewModel
import com.Juan.myapplication.databinding.FragmentPersonaBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonaFragment : Fragment() {
  private val viewModel: MainViewModel by viewModels()
  private var _binding: FragmentPersonaBinding? = null
  private val binding
    get() = _binding!!
  private var expanded = false
  private var persona: Persona? = null

  companion object {
    private const val ARG_PERSONA = "persona"

    fun newInstance(persona: Persona): PersonaFragment {
      return PersonaFragment().apply {
        arguments = Bundle().apply { putParcelable(ARG_PERSONA, persona) }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      persona =
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_PERSONA, Persona::class.java)
              } else {
                @Suppress("DEPRECATION") it.getParcelable(ARG_PERSONA)
              }
    }
            ?: throw IllegalArgumentException("No se proporcionaron argumentos")

    if (persona == null) {
      throw IllegalArgumentException("No se proporcionó una persona válida")
    }
  }

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    _binding = FragmentPersonaBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    persona?.let { actualizarUI(it) } ?: throw IllegalStateException("persona no puede ser null")
    setupClickListeners()
    observarMensajes()
  }

  private fun setupClickListeners() {
    binding.headerLayout.setOnClickListener {
      expanded = !expanded
      binding.expandableLayout.visibility = if (expanded) View.VISIBLE else View.GONE
    }

    binding.editarButton.text = "Agregar Pago"

    binding.editarButton.setOnClickListener {
      persona?.let { p ->
        val dialog = AgregarPagoDialogFragment.newInstance(p)
        dialog.listener =
                object : AgregarPagoDialogFragment.OnPagoAgregadoListener {
                  override fun onPagoAgregado() {
                    viewModel.cargarAlquileres()
                    viewModel.cargarEventosCalendario()
                    persona?.let { actualizarUI(it) }
                  }
                }
        dialog.show(parentFragmentManager, "AgregarPagoDialog")
      }
    }

    binding.eliminarButton.setOnClickListener {
      persona?.let { p ->
        AlertDialog.Builder(requireContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar a ${p.nombre}?")
                .setPositiveButton("Eliminar") { _, _ ->
                  viewModel.eliminarPersona(p)
                  viewModel.cargarEventosCalendario()
                }
                .setNegativeButton("Cancelar", null)
                .show()
      }
    }

    binding.whatsappButton.setOnClickListener { persona?.let { p -> abrirWhatsApp(p.telefono) } }
  }

  fun actualizarUI(persona: Persona) {
    binding.apply {
      nombreTextView.text = persona.nombre
      telefonoTextView.text = "Tel: ${persona.telefono}"
      diaInicialTextView.text = "Fecha inicial: ${persona.diaInicial}"
      cantPagadoTextView.text = "Pagado: $${persona.cantPagado}"
      alquilerInfoTextView.text =
              """
            Días: ${persona.alquiler.cantDias}
            Precio por día: $${persona.alquiler.precioPorDia}
            Total: $${persona.alquiler.precioTotal()}
            Falta pagar: $${persona.faltaPagar()}
        """.trimIndent()

      // Mostrar u ocultar botones según el estado
      editarButton.visibility = View.VISIBLE
      eliminarButton.visibility = View.VISIBLE
      whatsappButton.visibility = View.VISIBLE
    }
  }

  override fun onDestroyView() {
    // Ocultar el teclado si está visible
    view?.let { view ->
      val imm =
              requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Limpiar el binding
    super.onDestroyView()
    _binding = null
  }

  // Agregar este método helper para manejar el teclado
  private fun hideKeyboard() {
    view?.let { view ->
      val imm =
              requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }

  private fun observarMensajes() {
    viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
      mensaje?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
    }

    viewModel.error.observe(viewLifecycleOwner) { error ->
      error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
    }
  }

  private fun abrirWhatsApp(telefono: Long) {
    try {
      val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://wa.me/$telefono") }
      startActivity(intent)
    } catch (e: Exception) {
      Toast.makeText(requireContext(), "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show()
      Log.e("PersonaFragment", "Error al abrir WhatsApp", e)
    }
  }
}
