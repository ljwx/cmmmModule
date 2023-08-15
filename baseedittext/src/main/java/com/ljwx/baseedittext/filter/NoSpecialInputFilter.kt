package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * 禁止特殊字符
 */
class NoSpecialInputFilter  : InputFilter {

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
        val spec = "!@#\$%^&*()<>\\{}\\[]"
        // Reg表达式
        val pattern = Pattern.compile(spec)
        val matcher = pattern.matcher(source.toString())
        // 否有匹配结果
        return if (matcher.find()) {
            source.replace(Regex(spec), "")
        } else null
    }

}