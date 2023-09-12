package com.ljwx.baseapp.business

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.constant.LayoutStatus
import com.ljwx.baseapp.extensions.SingleClickListener

interface StateLayout {

    fun showStateLayout(@LayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int)

    fun setStateLayout(@LayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int)

    fun showContent()

    fun showLoading()

    fun showEmpty()

    fun showError()

    fun showOffline()

    fun setLayoutContent(@LayoutRes layout: Int)

    fun setLayoutLoading(@LayoutRes layout: Int)

    fun setLayoutEmpty(@LayoutRes layout: Int)

    fun setLayoutError(@LayoutRes layout: Int)

    fun setLayoutOffline(@LayoutRes layout: Int)

    fun setRetryListener(@IdRes id: Int, listener: SingleClickListener)

    fun setOtherListener(@IdRes id: Int, listener: SingleClickListener)
}