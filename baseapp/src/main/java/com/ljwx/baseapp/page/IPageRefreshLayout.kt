package com.ljwx.baseapp.page

import com.ljwx.baseapp.business.BaseRefreshLayout

interface IPageRefreshLayout {

    /**
     * 初始化下拉刷新布局
     */
    fun initRefreshLayout(refreshLayout: BaseRefreshLayout?)

    /**
     * 触发下拉刷新
     */
    fun onPullRefresh()

    /**
     * 刷新结束
     */
    fun pullRefreshFinish()
}