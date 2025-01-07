package com.Juan.myapplication.UI.View

import CalendarItemDecoration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.Juan.myapplication.Data.Model.CalendarEvent
import com.Juan.myapplication.UI.ViewModel.MainViewModel
import com.Juan.myapplication.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener {
  private var _binding: FragmentCalendarBinding? = null
  private val binding
    get() = _binding!!
  private lateinit var selectedDate: LocalDate
  private val viewModel: MainViewModel by viewModels()

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCalendarBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.calendarRecyclerView.addItemDecoration(CalendarItemDecoration())
    selectedDate = LocalDate.now()
    setMonthView()
    setupButtons()
    observeEvents()
    viewModel.cargarEventosCalendario()
  }

  private fun setupButtons() {
    binding.previousButton.setOnClickListener {
      selectedDate = selectedDate.minusMonths(1)
      setMonthView()
    }

    binding.nextButton.setOnClickListener {
      selectedDate = selectedDate.plusMonths(1)
      setMonthView()
    }
  }

  private fun observeEvents() {
    viewModel.events.observe(viewLifecycleOwner) { events -> setMonthView(events) }
  }

  private fun setMonthView(events: List<CalendarEvent> = emptyList()) {
    binding.monthYearTV.text = monthYearFromDate(selectedDate)
    val daysInMonth = daysInMonthArray(selectedDate)
    val currentMonth = YearMonth.from(selectedDate)
    val calendarAdapter = CalendarAdapter(daysInMonth, events, currentMonth, this)
    val layoutManager = GridLayoutManager(requireContext(), 7)
    binding.calendarRecyclerView.layoutManager = layoutManager
    binding.calendarRecyclerView.adapter = calendarAdapter
  }

  private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
    val daysInMonthArray = ArrayList<String>()
    val yearMonth = YearMonth.from(date)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstOfMonth = selectedDate.withDayOfMonth(1)
    val dayOfWeek = firstOfMonth.dayOfWeek.value

    for (i in 1..42) {
      if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
        daysInMonthArray.add("")
      } else {
        daysInMonthArray.add((i - dayOfWeek).toString())
      }
    }
    return daysInMonthArray
  }

  private fun monthYearFromDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    return date.format(formatter)
  }

  override fun onItemClick(position: Int, dayText: String) {
    if (dayText.isNotEmpty()) {
      val selectedDate = selectedDate.withDayOfMonth(dayText.toInt())
      viewModel.events.value?.forEach { event ->
        if (selectedDate >= event.startDate && selectedDate <= event.endDate) {
          Toast.makeText(context, "Alquiler: ${event.title}", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    fun newInstance() = CalendarFragment()
  }
}
