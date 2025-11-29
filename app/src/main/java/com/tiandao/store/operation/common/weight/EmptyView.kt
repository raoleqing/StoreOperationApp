package com.tiandao.store.operation.common.weight

// EmptyView.kt
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tiandao.store.operation.R

class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var emptyIcon: ImageView
    private var emptyMessage: TextView

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.layout_empty_state, this, true)

        emptyIcon = findViewById(R.id.empty_icon)
        emptyMessage = findViewById(R.id.empty_message)

        // 处理自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)

        try {
            val iconRes = typedArray.getResourceId(R.styleable.EmptyView_emptyIcon, R.mipmap.ic_empty_state)
            emptyIcon.setImageResource(iconRes)

            val message = typedArray.getString(R.styleable.EmptyView_emptyMessage)
                ?: context.getString(R.string.empty_default_message)
            emptyMessage.text = message

        } finally {
            typedArray.recycle()
        }
    }

    fun setEmptyIcon(resId: Int) {
        emptyIcon.setImageResource(resId)
    }

    fun setEmptyMessage(message: String) {
        emptyMessage.text = message
    }

}