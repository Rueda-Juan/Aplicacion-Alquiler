package com.Juan.myapplication.UI.View

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Juan.myapplication.R

class CalendarViewHolder(
        itemView: View,
        private val onItemListener: CalendarAdapter.OnItemListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

  val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
  val eventTitle: TextView = itemView.findViewById(R.id.eventTitleText)

  init {
    itemView.setOnClickListener(this)
  }

  override fun onClick(view: View) {
    try {
      val dayText = dayOfMonth.text.toString()
      if (dayText.isNotEmpty()) {
        onItemListener.onItemClick(adapterPosition, dayText)
      }
    } catch (e: Exception) {
      Log.e("CalendarViewHolder", "Error en onClick: ${e.message}")
    }
  }
}
