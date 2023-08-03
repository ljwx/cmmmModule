package com.ljwx.baseswitchenv

import android.content.Intent
import android.os.Process
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.registerShakeEnv(debug: Boolean, callback: ShakeSelectAppEnv.EnvCallback) {
    if (debug) {
        lifecycle.addObserver(ShakeSelectAppEnv(this) {
            callback.selected(it)
            Toast.makeText(this, "选中${it.title} ,正在重启", Toast.LENGTH_LONG).show()

            //重新打开app启动页
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            //杀掉以前进程
            Process.killProcess(Process.myPid())
        })
    }
}