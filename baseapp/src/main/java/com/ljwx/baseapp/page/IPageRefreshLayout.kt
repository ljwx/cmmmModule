package com.ljwx.baseapp.page

import androidx.annotation.IdRes
import com.ljwx.baseapp.R
import com.ljwx.baseapp.view.IViewRefreshLayout

interface IPageRefreshLayout {

    fun enableRefresh(): Boolean

    /**
     * 初始化下拉刷新布局
     */
    fun initRefreshLayout(refreshLayout: IViewRefreshLayout?)
    fun initRefreshLayout(@IdRes refreshId: Int = R.id.base_app_page_refresh_layout)

    /**
     * 触发刷新
     */
    @Deprecated(message = "deprecated")
    fun onRefreshData(type: Long = 0)

    fun onLoadData(type: Long = 0)

    /**
     * 刷新结束
     */
    fun pullRefreshFinish()
}