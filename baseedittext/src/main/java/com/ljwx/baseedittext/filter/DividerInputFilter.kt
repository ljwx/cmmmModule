package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned

class DividerInputFilter(private val digits: Int = 4, private val divider: String = " ") :
    InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (end != 0) {
            if (source == divider) {
                return ""
            }
            val validLength = dest.replace(Regex(divider), "").length
            if (dest.length >= digits && validLength % digits == 0) {
                return "$divider$source"
            }
        }
        return null
    }
}