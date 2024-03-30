package com.ljwx.baseapp.view

import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.constant.BaseLayoutStatus

interface IViewStateLayout {

    fun showStateLayout(@BaseLayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int)

    fun setStateLayout(@BaseLayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int)

    fun setContent(view: View)

    fun showContent(tag: Any? = null)

    fun showLoading(tag: Any? = null)

    fun showEmpty(tag: Any? = null)

    fun showError(tag: Any? = null)

    fun showOffline(tag: Any? = null)

    fun setLayoutContent(@LayoutRes layout: Int)

    fun setLayoutLoading(@LayoutRes layout: Int)

    fun setLayoutEmpty(@LayoutRes layout: Int)

    fun setLayoutError(@LayoutRes layout: Int)

    fun setLayoutOffline(@LayoutRes layout: Int)

    fun setClickListener(@IdRes id: Int, listener: OnClickListener)

//    fun onLoading()
//
//    fun onRefresh()
}