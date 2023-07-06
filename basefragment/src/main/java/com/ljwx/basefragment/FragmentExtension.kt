package com.ljwx.basefragment

import android.content.Intent
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gyf.immersionbar.ImmersionBar
import com.ljwx.baseapp.extensions.showToast


/**
 * 设置状态栏颜色
 *
 * @param fontDark 状态栏图标是否深色
 * @param barColor 状态栏背景颜色
 */
fun Fragment.setStatusBarColor(fontDark: Boolean, @ColorRes barColor: Int) {
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
fun Fragment.sendFinishAction(action: String) {
    val intent = Intent(action)
    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
}

fun Fragment.showToast(content: String?) {
    context?.showToast(content)
}