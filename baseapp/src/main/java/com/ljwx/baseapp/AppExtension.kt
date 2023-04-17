package com.ljwx.baseapp

import android.content.Context
import android.os.Looper
import android.view.Gravity

/**
 * 显示Toast弹窗
 */
fun Context.showToast(content: String?, gravity: Int = Gravity.BOTTOM, repeat: Boolean = false) {

}

/**
 * 当前是否是主线程
 */
val isMainThread: Boolean
    get() {
        return Looper.getMainLooper() == Looper.myLooper()
    }

