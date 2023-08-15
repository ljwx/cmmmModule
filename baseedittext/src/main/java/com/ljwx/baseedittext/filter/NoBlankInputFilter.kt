package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned

/**
 * 禁止输入空格
 */
open class NoBlankInputFilter : InputFilter {

    private val regex = Regex(" ")

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
            return source.replace(regex, "")
        }
        return null
    }

}