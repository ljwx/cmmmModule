package com.ljwx.baseapp.debug

import android.util.Log

open class RunDebugProxy(protected val type: Int) : Runnable {

    protected val TAG = "RunDebug-" + this.javaClass.simpleName

    override fun run() {
        Log.d(TAG, "debug run, type:$type")
    }

}