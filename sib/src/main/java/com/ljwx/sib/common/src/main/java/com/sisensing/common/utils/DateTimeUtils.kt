package com.sisensing.common.utils

import android.content.Context
import com.blankj.utilcode.util.StringUtils
import com.sisensing.common.R

object DateTimeUtils {

    /**
     * 数字月份转简称
     */
    @JvmStatic
    fun numMonth2Short(context: Context?, month: Int?): String {
        var m = ""
        if (context == null) {
            return m
        }
        if (month != null && month > 0 && month <= 12) {
            m = context.resources.getStringArray(R.array.month_array).get(month - 1)
        }
        return m
    }

    /**
     * 天最少显示2位
     */
    fun limit2Bit(day: Int?): String {
        var d = ""
        day?.let {
            d = if (day > 9) day.toString() else "0$day"
        }
        return d
    }

}