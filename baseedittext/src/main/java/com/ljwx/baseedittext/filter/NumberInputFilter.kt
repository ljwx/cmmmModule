package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned

/**
 * 只能输入数字
 */
open class NumberInputFilter : InputFilter {

    private val enableChars = mutableListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    /**
     * @param source 输入的文字
     * @param start 输入-0，删除-0
     * @param end 输入-文字的长度，删除-0
     * @param dest 原先显示的内容
     * @param dstart 输入-原光标位置，删除-光标删除结束位置
     * @param dend  输入-原光标位置，删除-光标删除开始位置
     * @return
     */
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        // 非删除
        if (end != 0) {
            // 粘贴的话需要过滤
            val modify = SpannableStringBuilder()
            for (i in 0 until end) {
                val c = source[i]
                if (c in enableChars) {
                    modify.append(c)
                }
            }
            return modify
        }
        return null
    }

    fun addChar(c: Char) {
        enableChars.add(c)
    }
}