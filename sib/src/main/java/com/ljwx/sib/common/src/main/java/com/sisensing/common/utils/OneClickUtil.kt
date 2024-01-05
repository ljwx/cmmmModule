package com.sisensing.common.utils

object OneClickUtil {

    private var lastClickTime = 0L


    /**
     * 防止点击抖动，给JAVA调用。1秒内不能点击
     */
    fun canClick():Boolean{
        val time = lastClickTime
        lastClickTime = System.currentTimeMillis()
        return System.currentTimeMillis() - time >= 1000
    }
}