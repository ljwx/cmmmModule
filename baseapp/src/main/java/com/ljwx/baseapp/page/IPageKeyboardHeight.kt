package com.ljwx.baseapp.page

import android.view.View

interface IPageKeyboardHeight {

    fun enableKeyboardHeightListener(): Boolean

    fun createKeyboardHeightProvider()

    fun keyboardHeightRootView(): View?

    fun setKeyboardHeightListener()

    fun isKeyboardShow(height: Int, buffHeight: Int): Boolean

    fun onKeyboardHeightChange(show: Boolean, height: Int)

}