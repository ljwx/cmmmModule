package com.ljwx.recyclerview

import android.util.Log

object BaseRecyclerLog {

    private val TAG = "-[recycler"

    fun i(tag: String, msg: String) {
        Log.i(tag + TAG, msg)
    }

    fun d(tag: String, msg: String) {
        Log.d(tag + TAG, msg)
    }

    fun w(tag: String, msg: String) {
        Log.w(tag + TAG, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag + TAG, msg)
    }

}