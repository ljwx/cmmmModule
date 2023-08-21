package com.ljwx.baseedittext.watcher

import android.text.Editable
import android.text.TextWatcher

open class LimitDecimalTextWatcher(private val digits: Int = 2) : TextWatcher {

    /**
     * @param s原内容
     * @param start  被替换内容起点坐标
     * @param count 被替换内容的长度
     * @param after 新增加内容的长度
     */
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    /**
     * @param s 发生改变后的内容
     * @param start  被替换内容的起点坐标
     * @param before 被替换内容的长度
     * @param count 新增加的内容的长度
     */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    /**
     * @param s 发生改变后的内容(对s编辑同样会触发TextWatcher)
     */
    override fun afterTextChanged(s: Editable) {
        val temp = s.toString()
        val posDot = temp.indexOf(".")
        if (posDot > 0 && (temp.length - posDot - 1 > digits)) {
            s.delete(posDot + 3, posDot + 4)
        }
    }
}