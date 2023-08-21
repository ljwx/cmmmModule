package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned

open class LimitDecimalInputFilter(private val digits: Int = 2) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (end != 0) {
            //直接输入.
            if (dest.isEmpty() && source == ".") {
                return "0."
            }
            //直接输入0
            if (dest.isEmpty() && source == "0") {
                return "0."
            }
            //已经输入小数点
            if (dest.contains(".")) {
                val pointIndex = dest.indexOf(".")
                //在小数点之前输入数字
                if (dstart <= pointIndex && dend <= pointIndex) {
                    return null
                }
                //已经有两位小数
                if (dest.substring(pointIndex + 1).length >= digits) {
                    return ""
                }
            } else {
                //在前面输入. 暂时不让输
//                if (source.contains(".")) {
//                    if (dest.length - digits > dend) {
//                        return ""
//                    }
//                }
            }
        }
        return null
    }
}