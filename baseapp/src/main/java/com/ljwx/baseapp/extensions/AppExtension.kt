package com.ljwx.baseapp.extensions

import android.os.Looper
import androidx.annotation.StringRes
import com.blankj.utilcode.util.StringUtils


/**
 * 当前是否是主线程
 */
val isMainThread: Boolean
    get() {
        return Looper.getMainLooper() == Looper.myLooper()
    }

