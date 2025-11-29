package com.tiandao.store.operation.common.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class CustomDividerItemDecoration(context: Context, private val orientation: Int) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private val divider = ColorDrawable()

    init {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL")
        }
    }

    fun setDividerDrawable(color: Int, height: Int) {
        if (height < 0) {
            throw IllegalArgumentException("Height must be non-negative")
        }

        // 设置颜色
        divider.color = color

        // 设置高度
        val outRect = Rect()
        if (orientation == HORIZONTAL) {
            outRect.bottom = height
        } else {
            outRect.right = height
        }

        // 确保在主线程中执行
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler(Looper.getMainLooper()).post {
                this.divider.setBounds(outRect)
            }
        } else {
            this.divider.setBounds(outRect)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == HORIZONTAL) {
            outRect.set(0, 0, 0, divider.bounds.height())
        } else {
            outRect.set(0, 0, divider.bounds.width(), 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == HORIZONTAL) {
            drawHorizontalDividers(c, parent)
        } else {
            drawVerticalDividers(c, parent)
        }
    }

    private fun drawHorizontalDividers(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.bounds.height()

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private fun drawVerticalDividers(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.right + params.rightMargin
            val right = left + divider.bounds.width()

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}

