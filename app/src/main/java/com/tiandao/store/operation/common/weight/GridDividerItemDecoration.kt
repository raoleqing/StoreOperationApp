package com.tiandao.store.operation.common.weight

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridDividerItemDecoration(
    private val spacing: Int, // 间距大小（单位：像素）
    private val spanCount: Int, // 网格列数
    private val includeEdge: Boolean = true // 是否包含边缘间距
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // 获取item位置
        val column = position % spanCount // 计算列索引

        if (includeEdge) {
            // 包含边缘的情况
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // 顶部行
                outRect.top = spacing
            }
            outRect.bottom = spacing // item底部间距
        } else {
            // 不包含边缘的情况
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item顶部间距
            }
        }
    }
}