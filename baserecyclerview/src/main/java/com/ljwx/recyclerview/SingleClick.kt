package com.ljwx.recyclerview

import android.view.View
import java.util.concurrent.TimeUnit

/**
 * 设置防抖动的点击事件
 * @param interval 间隔时间
 * @param unit 时间单位
 * @param block 点击回调
 */
fun View.singleClick(
    interval: Long = 600,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit
) {
    setOnClickListener(SingleClickListener(interval, unit, block))
}

internal class SingleClickListener(
    private val interval: Long = 600,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: View.() -> Unit
) : View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v)
        }

    }
}