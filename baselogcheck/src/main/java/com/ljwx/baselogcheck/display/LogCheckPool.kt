package com.ljwx.baselogcheck.display

import android.util.Log

object LogCheckPool {

    private var defaultSize = 2000
    private val logMap = HashMap<String, FixSizeVector<CharSequence>>()

    fun setDefaultSize(size: Int) {
        defaultSize = size
    }

    fun getLogPool(category: String): FixSizeVector<CharSequence> {
        var log = logMap[category]
        if (log == null) {
            log = FixSizeVector(defaultSize)
            logMap[category] = log
        }
        return log
    }
}