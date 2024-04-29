package com.ljwx.baseapp.page

import androidx.annotation.ColorRes
import com.ljwx.baseapp.view.IViewStatusBar

interface IPageStatusBar {

    /**
     * 获取StatusBar工具
     */
    fun getStatusBar(): IViewStatusBar

    /**
     * 初始化StatusBar
     */
    fun setStatusBar(
        @ColorRes backgroundColor: Int = android.R.color.white,
        fontDark: Boolean = true
    ): IViewStatusBar

    fun setStatusBarLight(light: Boolean)

    fun setStatusBarTransparent(transparent: Boolean = true)

}