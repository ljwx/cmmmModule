package com.ljwx.recordcheck

import android.app.Activity
import android.os.Build

fun Activity.startMultiWindowActivity(target: Class<*>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        MultiWindowUtils.startMultiWindowActivity(this, target)
    }
}