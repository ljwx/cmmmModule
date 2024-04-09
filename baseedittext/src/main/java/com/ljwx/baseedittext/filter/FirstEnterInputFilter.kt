package com.ljwx.baseedittext.filter

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

/**
 * 首个输入不能为换行
 */
class FirstEnterInputFilter : InputFilter {

    private val filter = "\n"

    /**
     * @param source 即将输入的文字
     * @param start 输入-0，删除-0
     * @param end 输入-文字的长度，删除-0
     * @param dest 输入框中原来的内容
     * @param dstart 光标所在位置
     * @param dend  光标终止位置
     * @return
     */
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        if (dstart == 0 && end != 0 && source.isNotEmpty()) {
            if (filter == source) {
                return ""
            } else if (filter == source.first().toString()) {
                return source.trimStart()
            }
        }
        return null
    }
}