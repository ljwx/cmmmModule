package com.ljwx.baseactivity

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.debug.ILogCheckRecyclerView
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.baseapp.shake.registerShake

/**
 * 获取Activity根布局
 */
val AppCompatActivity.rootLayout: View?
    get() {
        val container = findViewById<ViewGroup>(android.R.id.content)
        return if (container.childCount > 0) container.getChildAt(0) else container
    }

fun FragmentActivity.restart() {
    //重新打开app启动页
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    //杀掉以前进程
    Process.killProcess(Process.myPid())
}

fun FragmentActivity.logCheckDebugEx(open: Boolean) {
    val recycler = findViewById<View>(com.ljwx.baseapp.R.id.base_log_check_recycler) ?: return
    registerShake(open) {
        var visible = recycler.visibility == View.VISIBLE
        recycler.visibleGone(!visible)
    }
    if (open && recycler is ILogCheckRecyclerView) {
        recycler.run(lifecycleScope)
    }
}

inline fun <reified F : Fragment> FragmentActivity.fragmentInstanceEx(fromType: Int): F? {
    kotlin.runCatching {
        val fragment = F::class.java.newInstance()
        val bundle = Bundle()
        bundle.putInt(BaseConstBundleKey.FROM_TYPE, fromType)
        fragment.arguments = bundle
        return fragment
    }
    return null
}