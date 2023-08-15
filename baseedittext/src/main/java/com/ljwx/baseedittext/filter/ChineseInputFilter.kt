package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class ChineseInputFilter(private val inputEnable: Boolean = false) : InputFilter {

    private val regex =
        "^(?:[\\u3400-\\u4DB5\\u4E00-\\u9FEA\\uFA0E\\uFA0F\\uFA11\\uFA13\\uFA14\\uFA1F\\uFA21\\uFA23\\uFA24\\uFA27-\\uFA29]|[\\uD840-\\uD868\\uD86A-\\uD86C\\uD86F-\\uD872\\uD874-\\uD879][\\uDC00-\\uDFFF]|\\uD869[\\uDC00-\\uDED6\\uDF00-\\uDFFF]|\\uD86D[\\uDC00-\\uDF34\\uDF40-\\uDFFF]|\\uD86E[\\uDC00-\\uDC1D\\uDC20-\\uDFFF]|\\uD873[\\uDC00-\\uDEA1\\uDEB0-\\uDFFF]|\\uD87A[\\uDC00-\\uDFE0])+\$"

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (end != 0) {
            if (source.length == 1) {
                return if (Pattern.matches(regex, source)) {
                    if (inputEnable) null else ""
                } else {
                    if (inputEnable) "" else null
                }
            } else if (end > 1) {
                val builder = StringBuilder()
                source.forEach {
                    val char = it.toString()
                    val result = if (Pattern.matches(regex, char)) {
                        if (inputEnable) char else ""
                    } else {
                        if (inputEnable) "" else char
                    }
                    builder.append(result)
                }
                return builder.toString()
            }
        }
        return null
    }

}