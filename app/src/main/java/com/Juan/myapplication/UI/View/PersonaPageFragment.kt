package com.Juan.myapplication.UI.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.Juan.myapplication.R
import com.Juan.myapplication.UI.ViewModel.MainViewModel
import com.Juan.myapplication.databinding.FragmentPersonaPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonaPageFragment : Fragment() {
    private var _binding: FragmentPersonaPageBinding? = null
    private val binding
        get() = _binding!!
    val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonaPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cargarAlquileres()
        observePersonas()
        setupFab()
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            IngresarPersonaDialogFragment.newInstance(
                            object : OnPersonaGuardadaListener {
                                override fun onPersonaGuardada() {
                                    viewModel.cargarAlquileres()
                                }
                            }
                    )
                    .show(parentFragmentManager, IngresarPersonaDialogFragment.TAG)
        }
    }

    private fun observePersonas() {
        viewModel.alquileres.observe(viewLifecycleOwner) { personas ->
            Log.d("PersonaPageFragment", "Recibida lista de personas: ${personas.size}")
            // Limpiar contenedor antes de agregar nuevos fragments
            binding.personasContainer.removeAllViews()

            // Asegurarnos de que la lista no sea nula
            if (personas != null) {
                // Remover fragments antiguos
                childFragmentManager.fragments.forEach { fragment ->
                    childFragmentManager.beginTransaction().remove(fragment).commit()
                }
                childFragmentManager.executePendingTransactions()

                // Agregar cada persona en un nuevo fragment
                personas.forEach { persona ->
                    val fragment = PersonaFragment.newInstance(persona)
                    childFragmentManager
                            .beginTransaction()
                            .add(binding.personasContainer.id, fragment)
                            .commit()
                }

                // Mostrar mensaje si no hay personas
                if (personas.isEmpty()) {
                    val emptyText =
                            TextView(context).apply {
                                text = "No hay personas registradas"
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                layoutParams =
                                        LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                )
                                                .apply {
                                                    topMargin =
                                                            resources.getDimensionPixelSize(
                                                                    R.dimen.margin_normal
                                                            )
                                                }
                            }
                    binding.personasContainer.addView(emptyText)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PersonaPageFragment()
    }
}
