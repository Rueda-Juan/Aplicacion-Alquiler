package com.Juan.myapplication.UI.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.Juan.myapplication.Data.Model.Persona
import com.Juan.myapplication.R
import com.Juan.myapplication.UI.ViewModel.MainViewModel
import com.Juan.myapplication.databinding.FragmentAgregarPagoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgregarPagoDialogFragment : DialogFragment() {
  private var _binding: FragmentAgregarPagoBinding? = null
  private val binding
    get() = _binding!!
  private val viewModel: MainViewModel by viewModels()
  private lateinit var persona: Persona
  var listener: OnPagoAgregadoListener? = null

  interface OnPagoAgregadoListener {
    fun onPagoAgregado()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.DialogTheme)
  }

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAgregarPagoBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupAgregarButton()
  }

  private fun setupAgregarButton() {
    binding.agregarButton.setOnClickListener {
      val montoAdicional = binding.montoEditText.text.toString().toIntOrNull()
      if (montoAdicional != null) {
        agregarPago(montoAdicional)
      }
    }
  }

  private fun agregarPago(montoAdicional: Int) {
    lifecycleScope.launch {
      try {
        persona.cantPagado += montoAdicional
        viewModel.actualizarPersona(persona)
        listener?.onPagoAgregado()
        dismiss()
      } catch (e: Exception) {
        // Manejar error
      }
    }
  }

  fun setPersona(p: Persona) {
    persona = p
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    fun newInstance(persona: Persona) = AgregarPagoDialogFragment().apply { setPersona(persona) }
  }
}
