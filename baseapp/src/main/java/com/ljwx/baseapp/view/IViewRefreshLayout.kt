package com.ljwx.baseapp.view

import android.view.ViewGroup
import com.ljwx.baseapp.page.IPageRefreshLayout

interface IViewRefreshLayout {

    fun enableRefresh(enable: Boolean)

    fun setRefreshPage(refreshPage: IPageRefreshLayout)

    fun setRefreshHeader(header: IViewRefreshHeader)

    fun setOnRefreshListener(refreshListener: RefreshListener)

    fun refreshFinish()

    fun getView(): ViewGroup
    interface RefreshListener {
        fun onRefresh(refreshLayout: IViewRefreshLayout)
    }

}