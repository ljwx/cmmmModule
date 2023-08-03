package com.ljwx.baseapp.extensions

import android.content.Intent
import android.os.Process
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.restart() {
    //重新打开app启动页
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    //杀掉以前进程
    Process.killProcess(Process.myPid())
}