package com.ljwx.baseapp.page

import android.view.View
import androidx.annotation.IdRes
import com.ljwx.baseapp.business.BaseStateLayout
import com.ljwx.baseapp.constant.LayoutStatus

interface IPageStateLayout {
    /**
     * 初始化多状态布局
     */
    fun initStateLayout(stateLayout: BaseStateLayout?)

    /**
     * 显示多状态
     */
    fun showStateLayout(
        @LayoutStatus.LayoutStatus state: Int,
        show: Boolean = true,
        tag: Any? = null
    )

    /**
     * 设置点击事件
     */
    fun setStateLayoutClick(
        @IdRes id: Int,
        listener: View.OnClickListener,
        @LayoutStatus.LayoutStatus vararg stateLayout: Int
    )

    /**
     * 多状态布局里的错误重试
     */
    fun onStateLayoutRetry(tag: Any? = null)
}