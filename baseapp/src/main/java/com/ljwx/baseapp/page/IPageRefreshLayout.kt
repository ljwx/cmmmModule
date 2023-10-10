package com.ljwx.baseapp.page

import com.ljwx.baseapp.view.IViewRefreshLayout

interface IPageRefreshLayout {

    /**
     * 初始化下拉刷新布局
     */
    fun initRefreshLayout(refreshLayout: IViewRefreshLayout?)

    /**
     * 触发刷新
     */
    fun onRefreshData()

    /**
     * 刷新结束
     */
    fun pullRefreshFinish()
}