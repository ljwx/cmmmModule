package com.ljwx.baseapp.page

import com.drake.statelayout.StateLayout
import com.ljwx.baseapp.LayoutStatus
import com.scwang.smart.refresh.layout.SmartRefreshLayout

interface IBasePage {

    /**
     * 初始化多状态布局
     */
    fun initStateLayout(stateLayout: StateLayout?, retryId: Int? = null)

    /**
     * 初始化下拉刷新布局
     */
    fun initRefreshLayout(refreshLayout: SmartRefreshLayout?)

    /**
     * 是否显示加载悬浮弹窗
     */
    fun showPopupLoading(show: Boolean, type: Int = 0)

    /**
     * 悬浮弹窗是否显示中
     */
    fun isPopupLoadingShowing(): Boolean

    /**
     * 显示多状态
     */
    fun showStateLayout(state: LayoutStatus, show: Boolean = true, tag: Any? = null)

    /**
     * 多状态布局里的错误重试
     */
    fun onStateLayoutRetry(tag: Any? = null)

    /**
     * 触发下拉刷新
     */
    fun onPullRefresh()

    /**
     * 刷新结束
     */
    fun pullRefreshFinish()

    /**
     * 网络异常
     */
    fun networkException(exception: Exception)

}