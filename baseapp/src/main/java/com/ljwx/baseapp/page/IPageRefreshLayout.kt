package com.ljwx.baseapp.page

import com.ljwx.baseapp.business.IBaseRefreshLayout

interface IPageRefreshLayout {

    /**
     * 初始化下拉刷新布局
     */
    fun initRefreshLayout(refreshLayout: IBaseRefreshLayout?)

    /**
     * 触发下拉刷新
     */
    fun onPullRefresh()

    /**
     * 刷新结束
     */
    fun pullRefreshFinish()
}