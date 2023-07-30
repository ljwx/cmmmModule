package com.ljwx.baseapp.util

import android.util.Log

object MemoryUtils {

    private var maxMemory = -1L
    private var usedMemory = -1L
    private var mBytes = ByteArray(1024 * 1024 * 15)
    private var clickCount = 0.27

    fun getMaxMemory() {
        if (maxMemory > -1) {
            return
        }
        val runtime = Runtime.getRuntime()
        maxMemory = runtime.maxMemory()
    }

    fun getUsedMemory() {
        val runtime = Runtime.getRuntime()
        usedMemory = runtime.totalMemory() - runtime.freeMemory()
    }

    fun requestMemory() {
        if (!memoryLimit()) {
            clickCount += 0.33
            val size = (Runtime.getRuntime().freeMemory() * clickCount).toInt()
            mBytes = ByteArray(size)
        }
        getMaxMemory()
        getUsedMemory()
        showMemoryDetail()
    }

    private fun memoryLimit(): Boolean {
        if (usedMemory / (maxMemory * 1f) > 0.75) {
            return true
        }
        return false
    }

    fun showMemoryDetail() {
        val max = "最大可用:${maxMemory / 1024 / 1024}MB,"
        val use = "已经使用:${usedMemory / 1024 / 1024}MB,"
        val isExceededRatio = usedMemory > (3f * maxMemory) / 4f
        val ratio = "占用:${usedMemory / (maxMemory * 1f)}," + "是否大于3/4: $isExceededRatio"
        Log.d("ljwx2", max + use + ratio)
    }

}