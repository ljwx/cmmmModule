package com.ljwx.baseapp.extensions

import android.view.View

internal fun View.click(period: Long = 500, block: View.() -> Unit) {
    setOnClickListener(SingleClickListener(period, block))
}

internal fun View.singleClick(period: Long = 500, block: View.() -> Unit) {
    setOnClickListener(SingleClickListener(period, block))
}

private class SingleClickListener(private val period: Long = 500, private var block: View.() -> Unit) :
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