package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned

/**
 * 禁止输入换行
 */
class NoEnterInputFilter : InputFilter {

    private val regex = Regex("\n")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (end != 0) {
            return source.replace(regex, "")
        }
        return null
    }
}