package com.ljwx.baseswitchenv

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Process
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.registerShakeEnv(
    callback: ShakeSelectAppEnv.EnvCallback
) {
    if (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)) {
        executeSwitchEnv(callback)
    }
}

fun FragmentActivity.registerShakeEnv(
    debug: Boolean,
    callback: ShakeSelectAppEnv.EnvCallback
) {
    if (debug) {
        executeSwitchEnv(callback)
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal fun FragmentActivity.executeSwitchEnv(callback: ShakeSelectAppEnv.EnvCallback) {
    lifecycle.addObserver(ShakeSelectAppEnv(this) {
        callback.selected(it)
        Toast.makeText(this, "选中${it.title} ,正在重启", Toast.LENGTH_LONG).show()

        Thread.sleep(500)

        //重新打开app启动页
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        //杀掉以前进程
        Process.killProcess(Process.myPid())
    })
}
