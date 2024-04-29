package com.ljwx.baseapp.view

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes


interface IViewStatusBar {

    fun immersion(): IViewStatusBar

    fun darkMode(dark: Boolean = false, @ColorInt backgroundColor: Int): IViewStatusBar

    fun transparent(boolean: Boolean)

    fun setStatusBar(@ColorInt backgroundColor: Int, @ColorInt fontColor: Int): IViewStatusBar

    fun setCustomStatusBar(@ColorRes backgroundColor: Int, fontDark: Boolean): IViewStatusBar

}