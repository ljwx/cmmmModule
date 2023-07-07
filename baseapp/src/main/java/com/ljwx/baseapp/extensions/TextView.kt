package com.ljwx.baseapp.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.setMidLine(show: Boolean = true) {
    if (show) {
        paint.isAntiAlias = true //抗锯齿
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
    } else {
        paint.flags = 0
    }
}

fun TextView.setUnderline(show: Boolean = true) {
    if (show) {
        paint.isAntiAlias = true //抗锯齿
        paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    } else {
        paint.flags = 0
    }
}