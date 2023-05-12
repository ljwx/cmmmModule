package com.ljwx.baseapp.page

import androidx.annotation.ColorRes
import com.gyf.immersionbar.ImmersionBar

interface IPageStatusBar {

    /**
     * 获取StatusBar工具
     */
    fun getStatusBarUtils(): ImmersionBar

    /**
     * 初始化StatusBar
     */
    fun setStatusBar(
        @ColorRes backgroundColor: Int = android.R.color.white,
        fontDark: Boolean = true
    ): ImmersionBar

//    /**
//     * 网络异常
//     */
//    fun networkException(exception: Exception)

}