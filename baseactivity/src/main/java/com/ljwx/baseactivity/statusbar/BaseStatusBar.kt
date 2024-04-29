package com.ljwx.baseactivity.statusbar

import android.app.Activity
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.ljwx.baseapp.view.IViewStatusBar

open class BaseStatusBar : IViewStatusBar {

    private val statusBar: ImmersionBar

    constructor(activity: Activity) {
        statusBar = ImmersionBar.with(activity)
    }

    constructor(fragment: Fragment) {
        statusBar = ImmersionBar.with(fragment)
    }

    override fun immersion(): IViewStatusBar {
        statusBar.reset().transparentBar().init()
        return this
    }

    override fun darkMode(dark: Boolean, backgroundColor: Int): IViewStatusBar {
        if (dark) {
            statusBar
                .reset()
                .fitsSystemWindows(true)//默认为false，当为true时一定要指定statusBarColor()
                .statusBarColor(android.R.color.background_dark)
                .statusBarDarkFont(false)//状态栏字体是深色，不写默认为亮色
                .init()
        } else {
            statusBar
                .reset()
                .fitsSystemWindows(true)//默认为false，当为true时一定要指定statusBarColor()
                .statusBarColor(android.R.color.white)
                .statusBarDarkFont(true)//状态栏字体是深色，不写默认为亮色
                .init()
        }
        return this
    }

    override fun transparent(boolean: Boolean) {
        if (boolean) {
            statusBar.reset().transparentStatusBar().init()
        }
    }

    override fun setStatusBar(backgroundColor: Int, fontColor: Int): IViewStatusBar {
        return this
    }

    override fun setCustomStatusBar(backgroundColor: Int, fontDark: Boolean): IViewStatusBar {
        statusBar
            .reset()
            .fitsSystemWindows(true)//默认为false，当为true时一定要指定statusBarColor()
            .statusBarColor(backgroundColor)
            .statusBarDarkFont(fontDark)//状态栏字体是深色，不写默认为亮色
            .init()
        return this
    }
}