package com.ljwx.baseapp.extensions

import android.content.Intent
import android.os.Looper
import android.os.Process
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.StringUtils


/**
 * 当前是否是主线程
 */
val isMainThread: Boolean
    get() {
        return Looper.getMainLooper() == Looper.myLooper()
    }

fun AppCompatActivity.restart() {
    //重新打开app启动页
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    //杀掉以前进程
    Process.killProcess(Process.myPid())
}
