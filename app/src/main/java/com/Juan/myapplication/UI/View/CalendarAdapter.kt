package com.Juan.myapplication.UI.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Juan.myapplication.Data.Model.CalendarEvent
import com.Juan.myapplication.R
import java.time.LocalDate
import java.time.YearMonth

class CalendarAdapter(
        private val daysOfMonth: ArrayList<String>,
        private val events: List<CalendarEvent>,
        private val currentMonth: YearMonth,
        private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarViewHolder>() {

  private val today = LocalDate.now()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.calendar_day_cell, parent, false)
    val layoutParams = view.layoutParams
    layoutParams.height = (parent.height * 0.166666666).toInt()
    return CalendarViewHolder(view, onItemListener)
  }

  override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
    try {
      val dayText = daysOfMonth[position]
      holder.dayOfMonth.text = dayText

      if (dayText.isNotEmpty()) {
        val currentDate = currentMonth.atDay(dayText.toInt())

        // Resaltar día actual
        if (currentDate == today) {
          holder.itemView.setBackgroundResource(R.drawable.calendar_current_day)
        }

        val eventsForDay =
                events.filter { event ->
                  try {
                    // Un evento debe mostrarse si la fecha actual está entre la fecha de inicio y
                    // fin del evento
                    currentDate >= event.startDate && currentDate <= event.endDate
                  } catch (e: Exception) {
                    Log.e("CalendarAdapter", "Error comparando fechas: ${e.message}")
                    false
                  }
                }

        if (eventsForDay.isNotEmpty()) {
          holder.apply {
            eventTitle.text = eventsForDay.joinToString("\n") { it.title }
            eventTitle.visibility = View.VISIBLE
            itemView.setBackgroundResource(R.drawable.calendar_day_with_events)
          }
        } else if (currentDate != today) {
          holder.eventTitle.visibility = View.GONE
          holder.itemView.background = null
        }
      } else {
        holder.eventTitle.visibility = View.GONE
        holder.itemView.background = null
      }
    } catch (e: Exception) {
      Log.e("CalendarAdapter", "Error en onBindViewHolder", e)
      holder.eventTitle.visibility = View.GONE
      holder.itemView.background = null
    }
  }

  override fun getItemCount(): Int = daysOfMonth.size

  interface OnItemListener {
    fun onItemClick(position: Int, dayText: String)
  }
}
