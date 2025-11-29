package com.tiandao.store.operation.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.apply
import kotlin.let
import kotlin.text.isEmpty
import kotlin.text.isNullOrEmpty

object DateUtils {

    // 常用日期格式
    const val FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YMD_HM = "yyyy-MM-dd HH:mm"
    const val FORMAT_YMD = "yyyy-MM-dd"
    const val FORMAT_MD = "MM-dd"
    const val FORMAT_HMS = "HH:mm:ss"
    const val FORMAT_HM = "HH:mm"
    const val FORMAT_YM = "yyyy-MM"
    const val FORMAT_YMD_HMS_CN = "yyyy年MM月dd日 HH时mm分ss秒"
    const val FORMAT_YMD_CN = "yyyy年MM月dd日"
    const val FORMAT_MD_CN = "MM月dd日"

    /**
     * 获取当前时间字符串
     * @param format 日期格式，默认"yyyy-MM-dd HH:mm:ss"
     */
    fun getCurrentTime(format: String = FORMAT_YMD_HMS): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(Date())
    }

    /**
     * 日间格式化
     */
    fun dateFormat(date: Date?, format: String = FORMAT_YMD_HMS): String {
         if (date == null) {
            return ""
        }
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    /**
     * 日间格式化
     */
    fun dateStringFormat(dateString: String, inFormat: String = FORMAT_YMD_HMS, outFormat: String = FORMAT_YMD): String {
        if(dateString.isEmpty()){
            return ""
        }
        var date = SimpleDateFormat(inFormat, Locale.getDefault()).parse(dateString)
        return SimpleDateFormat(outFormat, Locale.getDefault()).format(date)
    }

    /**
     * 将时间戳转换为日期字符串
     * @param timestamp 时间戳(毫秒)
     * @param format 日期格式
     */
    fun timestampToString(timestamp: Long?, format: String = FORMAT_YMD_HMS): String {
        if(timestamp == null){
            return ""
        }
        return SimpleDateFormat(format, Locale.getDefault()).format(Date(timestamp))
    }
    /**
     * 将日期字符串转换为时间戳
     * @param date 日期字符串
     * @param format 日期格式
     */
    fun poseDate(date: Date?, format: String = FORMAT_YMD_HMS): String {
        if(date == null){
            return ""
        }
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    /**
     * 将日期字符串转换为时间戳
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun stringToTimestamp(dateString: String?, format: String = FORMAT_YMD_HMS): Long {
        if(dateString.isNullOrEmpty()){
            return 0L
        }
        return try {
            SimpleDateFormat(format, Locale.getDefault()).parse(dateString)?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 将日期字符串从一种格式转换为另一种格式
     * @param dateString 原始日期字符串
     * @param fromFormat 原始格式
     * @param toFormat 目标格式
     */
    fun convertDateFormat(
        dateString: String,
        fromFormat: String = FORMAT_YMD_HMS,
        toFormat: String = FORMAT_YMD
    ): String {
        return try {
            val date = SimpleDateFormat(fromFormat, Locale.getDefault()).parse(dateString)
            date?.let { SimpleDateFormat(toFormat, Locale.getDefault()).format(it) } ?: ""
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    // 使用 SimpleDateFormat 格式化日期
    fun formatDateForApi(date: String): String {
        val sdf = SimpleDateFormat(FORMAT_YMD, Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * 获取两个日期之间的天数差
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param format 日期格式
     */
    fun getDaysBetween(
        startDate: String,
        endDate: String,
        format: String = FORMAT_YMD
    ): Int {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            if (start != null && end != null) {
                ((end.time - start.time) / (1000 * 60 * 60 * 24)).toInt()
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取某日期的星期几
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getDayOfWeek(dateString: String, format: String = FORMAT_YMD): String {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SUNDAY -> "星期日"
                    Calendar.MONDAY -> "星期一"
                    Calendar.TUESDAY -> "星期二"
                    Calendar.WEDNESDAY -> "星期三"
                    Calendar.THURSDAY -> "星期四"
                    Calendar.FRIDAY -> "星期五"
                    Calendar.SATURDAY -> "星期六"
                    else -> ""
                }
            } else {
                ""
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取某日期的年份
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getYear(dateString: String, format: String = FORMAT_YMD): Int {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.YEAR)
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取当前年份
     */
    fun getNowYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.YEAR]
    }


    /**
     * 获取某日期的月份
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getMonth(dateString: String, format: String = FORMAT_YMD): Int {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.MONTH) + 1 // 月份从0开始
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取当前月份
     */
    fun getNowMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.MONTH] + 1
    }

    /**
     * 获取某日期的日
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getDay(dateString: String, format: String = FORMAT_YMD): Int {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.DAY_OF_MONTH)
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取当前月号
     */
    fun getNowDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.DATE]
    }


    /**
     * 获取当前时间戳(毫秒)
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取某日期的开始时间(00:00:00)的时间戳
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getStartOfDayString(dateString: String, informat: String = FORMAT_YMD,outformat: String = FORMAT_YMD_HMS): String {
        return try {
            val date = SimpleDateFormat(informat, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                SimpleDateFormat(outformat, Locale.getDefault()).format(Date(calendar.timeInMillis))
            } else {
                dateString
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }

    /**
     * 获取某日期的开始时间(00:00:00)的时间戳
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getStartOfDayTimestamp(dateString: String?, format: String = FORMAT_YMD): Long {
        if(dateString.isNullOrEmpty()){
            return 0
        }
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取某日期的结束时间(23:59:59)的时间戳
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getEndOfDayTimestamp(dateString: String, format: String = FORMAT_YMD): Long {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                calendar.timeInMillis
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取某日期的结束时间(23:59:59)的时间戳
     * @param dateString 日期字符串
     * @param format 日期格式
     */
    fun getEndOfDayString(dateString: String, informat: String = FORMAT_YMD,outformat: String = FORMAT_YMD_HMS): String {
        return try {
            val date = SimpleDateFormat(informat, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                SimpleDateFormat(outformat, Locale.getDefault()).format(Date(calendar.timeInMillis))
            } else {
                dateString
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }


    // 获取当月第一天 00:00:00
    fun getStartOfMonth(dateString: String, informat: String = FORMAT_YMD,outformat: String = FORMAT_YMD_HMS): String {
        return try {
            val date = SimpleDateFormat(informat, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat(outformat, Locale.getDefault()).format(Date(calendar.timeInMillis))
            } else {
                dateString
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }

    // 获取当月最后一天 23:59:59
    fun getEndOfMonth(dateString: String, informat: String = FORMAT_YMD,outformat: String = FORMAT_YMD_HMS): String  {
        return try {
            val date = SimpleDateFormat(informat, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                SimpleDateFormat(outformat, Locale.getDefault()).format(Date(calendar.timeInMillis))
            } else {
                dateString
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }

    }

    /**
     * 获取当前日期加上指定天数后的日期
     * @param days 要增加的天数
     * @param format 返回的日期格式
     */
    fun addDaysToCurrent(days: Int, format: String = FORMAT_YMD): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
    }

    /**
     * 获取指定日期加上指定天数后的日期
     * @param dateString 原始日期
     * @param days 要增加的天数
     * @param format 返回的日期格式
     */
    fun addDays(dateString: String, days: Int, format: String = FORMAT_YMD): String {
        return try {
            val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateString)
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.add(Calendar.DAY_OF_YEAR, days)
                SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
            } else {
                ""
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 判断是否为闰年
     * @param year 年份
     */
    fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
    }

    /**
     * 获取某个月份的天数
     * @param year 年份
     * @param month 月份(1-12)
     */
    fun getDaysInMonth(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * 判断两个日期是否为同一天
     * @param date1 日期1
     * @param date2 日期2
     * @param format 日期格式
     */
    fun isSameDay(date1: String, date2: String, format: String = FORMAT_YMD): Boolean {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val d1 = sdf.parse(date1)
            val d2 = sdf.parse(date2)
            d1?.time == d2?.time
        } catch (e: ParseException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 获取年龄
     * @param birthDate 出生日期
     * @param format 日期格式
     */
    fun getAge(birthDate: String, format: String = FORMAT_YMD): Int {
        return try {
            val birth = SimpleDateFormat(format, Locale.getDefault()).parse(birthDate)
            if (birth != null) {
                val now = Calendar.getInstance()
                val birthCal = Calendar.getInstance()
                birthCal.time = birth

                var age = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)
                if (now.get(Calendar.DAY_OF_YEAR) < birthCal.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }
                age
            } else {
                0
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 判断是否是今天
     */
    fun isToday(timestamp: Long): Boolean {
        val calendar = Calendar.getInstance()
        val today = calendar.apply {
            timeInMillis = System.currentTimeMillis()
        }

        val targetDate = calendar.apply {
            timeInMillis = timestamp
        }

        return today.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == targetDate.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == targetDate.get(Calendar.DAY_OF_MONTH)
    }

    fun getDate(year: Int, month: Int,  day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        val date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }


}