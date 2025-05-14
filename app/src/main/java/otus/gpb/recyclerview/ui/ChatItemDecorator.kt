package otus.gpb.recyclerview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import otus.gpb.recyclerview.R

class ChatItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_less_attention)
        strokeWidth = 0.5f
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)

            val positionCurrent = parent.getChildAdapterPosition(child)
            if (positionCurrent != RecyclerView.NO_POSITION) {
                val lastElementPosition = parent.adapter?.itemCount?.minus(1)
                if (positionCurrent != lastElementPosition) {
                    c.drawLine(
                        (child.findViewById<CardView>(R.id.img_user_avatar_container).right +
                                child.findViewById<TextView>(R.id.txt_user_name).left.toFloat()) / 2,
                        bounds.bottom.toFloat(),
                        bounds.right.toFloat(),
                        bounds.bottom.toFloat(),
                        paint,
                    )
                }
            }
        }
    }
}