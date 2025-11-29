package com.tiandao.store.operation.common.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import com.tiandao.store.operation.databinding.DateSelectDialogBinding
import com.tiandao.store.operation.utils.DateUtils
import java.util.Calendar
import java.util.Locale
import kotlin.let
import kotlin.text.format

class DateSelectDialog (private val activity: Activity, private val showDay : Boolean,
                        private val listener: OnFilterAppliedListener) : AppCompatDialog(activity)  {

    private var binding: DateSelectDialogBinding? = null
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0
    private var selectDate: String = ""

    interface OnFilterAppliedListener {
        fun determine(dialog: DateSelectDialog, date : String)
        fun close(dialog: DateSelectDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 透明背景
        binding = DateSelectDialogBinding.inflate(layoutInflater)
        binding?.let {
            setContentView(it.root)
            window?.let { item ->
                val params = item.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.gravity = Gravity.BOTTOM
                item.attributes = params
            }

            // 获取当前日期
            val calendar = Calendar.getInstance()
            selectedYear = calendar.get(Calendar.YEAR)
            selectedMonth = calendar.get(Calendar.MONTH) + 1
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

            // 设置年份选择器（前后20年范围）
            it.yearPicker.minValue = selectedYear - 20
            it.yearPicker.maxValue = selectedYear + 20
            it.yearPicker.value = selectedYear
            it.yearPicker.wrapSelectorWheel = false

            // 设置月份选择器
            it.monthPicker.minValue = 1
            it.monthPicker.maxValue = 12
            it.monthPicker.value = selectedMonth
            it.monthPicker.displayedValues = arrayOf(
                "1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月"
            )

            // 设置日期选择器
            val daysInMonth = getDaysInMonth(selectedYear, selectedMonth)
            it.dayPicker.minValue = 1
            it.dayPicker.maxValue = daysInMonth
            it.dayPicker.value = selectedDay

            if (showDay) {
                it.dayPicker.visibility = ViewGroup.VISIBLE
            } else {
                it.dayPicker.visibility = ViewGroup.GONE
            }

            // 设置监听器
            it.yearPicker.setOnValueChangedListener { _, _, newVal ->
                selectedYear = newVal
                updateDayPicker(selectedYear, selectedMonth)
                updateSelectedDateText()
            }

            it.monthPicker.setOnValueChangedListener { _, _, newVal ->
                selectedMonth = newVal
                updateDayPicker(selectedYear, selectedMonth)
                updateSelectedDateText()
            }

            it.dayPicker.setOnValueChangedListener { _, _, newVal ->
                selectedDay = newVal
                updateSelectedDateText()
            }

            // 更新显示的日期文本
            updateSelectedDateText()

//            // 今天按钮
//            binding.todayButton.setOnClickListener {
//                val calendar = Calendar.getInstance()
//                selectedYear = calendar.get(Calendar.YEAR)
//                selectedMonth = calendar.get(Calendar.MONTH) + 1
//                selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
//
//                binding.yearPicker.value = selectedYear
//                binding.monthPicker.value = selectedMonth
//                updateDayPicker(selectedYear, selectedMonth)
//                binding.dayPicker.value = selectedDay
//
//                updateSelectedDateText()
//            }

            it.ivClose.setOnClickListener {
                listener.close( this)
            }
            it.cancelButton.setOnClickListener {
                listener.close( this)
            }

            it.confirmButton.setOnClickListener {
                listener.determine(this, selectDate)
            }

        }

    }


    private fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // 月份是从0开始的
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun updateDayPicker(year: Int, month: Int) {
        val daysInMonth = getDaysInMonth(year, month)
        val currentDay = binding?.dayPicker?.value?: 0

        binding?.dayPicker?.minValue = 1
        binding?.dayPicker?.maxValue = daysInMonth

        binding?.dayPicker?.value = currentDay
        selectedDay = currentDay
    }


    private fun updateSelectedDateText() {
        selectDate = String.format(Locale.getDefault(),
            "%d-%02d-%02d", selectedYear, selectedMonth, selectedDay)
        if(showDay){
            binding?.tvSelectedDate?.text = selectDate
        }else{
            binding?.tvSelectedDate?.text = DateUtils.getStartOfDayString( selectDate, DateUtils.FORMAT_YMD, DateUtils.FORMAT_YM)
        }

    }


}