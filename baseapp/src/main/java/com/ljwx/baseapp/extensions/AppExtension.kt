package com.ljwx.baseapp.extensions

import android.os.Looper

/**
 * 当前是否是主线程
 */
val isMainThread: Boolean
    get() {
        return Looper.getMainLooper() == Looper.myLooper()
    }


