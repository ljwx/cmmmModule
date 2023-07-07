package com.ljwx.baseapp.extensions

import android.view.View

@Deprecated(message = "请使用singleClick", replaceWith = ReplaceWith(expression = "singleClick"))
inline fun View.click(period: Long = 500, noinline block: View.() -> Unit) {
    setOnClickListener(SingleClickListener(period, block))
}

inline fun View.singleClick(period: Long = 500, noinline block: View.() -> Unit) {
    setOnClickListener(SingleClickListener(period, block))
}

class SingleClickListener(private val period: Long = 500, private var block: View.() -> Unit) :
    View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > period) {
            lastTime = currentTime
            block(v)
        }
    }
}