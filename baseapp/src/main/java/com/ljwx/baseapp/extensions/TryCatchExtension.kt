package com.ljwx.baseapp.extensions

import android.util.Log


inline fun <T> T.doWithTry(logTag: String = "doWithTry", block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
        Log.d(logTag, "报错")
    }
}