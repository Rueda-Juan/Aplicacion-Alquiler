import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CalendarItemDecoration : RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
          outRect: Rect,
          view: View,
          parent: RecyclerView,
          state: RecyclerView.State
  ) {
    outRect.left = 4
    outRect.right = 4
    outRect.top = 4
    outRect.bottom = 4
  }

  override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    val paint =
            Paint().apply {
              color = Color.parseColor("#E0E0E0")
              strokeWidth = 1.0f
            }

    for (i in 0 until parent.childCount) {
      val child = parent.getChildAt(i)

      // Línea derecha
      c.drawLine(
              child.right.toFloat(),
              child.top.toFloat(),
              child.right.toFloat(),
              child.bottom.toFloat(),
              paint
      )

      // Línea inferior
      c.drawLine(
              child.left.toFloat(),
              child.bottom.toFloat(),
              child.right.toFloat(),
              child.bottom.toFloat(),
              paint
      )
    }
  }
}
