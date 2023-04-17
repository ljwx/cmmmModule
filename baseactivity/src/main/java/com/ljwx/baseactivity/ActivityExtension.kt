package com.ljwx.baseactivity

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gyf.immersionbar.ImmersionBar
import com.ljwx.baseapp.showToast

/**
 * 获取Activity根布局
 */
val AppCompatActivity.rootLayout: View?
    get() {
        val container = findViewById<ViewGroup>(android.R.id.content)
        return if (container.childCount > 0) container.getChildAt(0) else container
    }


/**
 * 初始化Toolbar
 */
fun AppCompatActivity.initToolbar(@IdRes toolbarId: Int? = null): Toolbar? {
    // 使用通用id或自定义id
    if (supportActionBar == null) {
        val toolbar = findViewById(toolbarId ?: R.id.base_activity_toolbar) as? Toolbar
        setSupportActionBar(toolbar)
        toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        return toolbar
    } else {
        return null
    }
}

/**
 * 设置Toolbar标题
 *
 * @param title 标题内容
 */
fun AppCompatActivity.setToolbarTitle(title: CharSequence) {
    supportActionBar?.title = title
}


/**
 * 设置状态栏颜色
 *
 * @param fontDark 状态栏图标是否深色
 * @param barColor 状态栏背景颜色
 */
fun AppCompatActivity.setStatusBarColor(fontDark: Boolean, @ColorRes barColor: Int) {
    ImmersionBar.with(this)
        .reset()//解决状态栏和布局重叠问题
        .fitsSystemWindows(true)//默认为false，当为true时一定要指定statusBarColor()
        .statusBarColor(barColor)
        .statusBarDarkFont(fontDark)//状态栏字体是深色，不写默认为亮色
        .init()
}

/**
 * 结束Activity
 *
 * @param action action标记
 */
fun Activity.sendFinishAction(action: String) {
    val intent = Intent(action)
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

fun Activity.showToast(content: String?) {
    showToast(content)
}