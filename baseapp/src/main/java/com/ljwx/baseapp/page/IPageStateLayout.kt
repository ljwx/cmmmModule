package com.ljwx.baseapp.page

import com.drake.statelayout.StateLayout
import com.ljwx.baseapp.LayoutStatus

interface IPageStateLayout {
    /**
     * 初始化多状态布局
     */
    fun initStateLayout(stateLayout: StateLayout?, retryId: Int? = null)

    /**
     * 显示多状态
     */
    fun showStateLayout(state: LayoutStatus, show: Boolean = true, tag: Any? = null)

    /**
     * 多状态布局里的错误重试
     */
    fun onStateLayoutRetry(tag: Any? = null)
}