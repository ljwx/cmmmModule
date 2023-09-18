package com.ljwx.baseapp.view

interface IViewRefreshLayout {

    fun setRefreshHeader(header: IViewRefreshHeader)

    fun setOnRefreshListener(refreshListener: RefreshListener)

    fun refreshFinish()

    interface RefreshListener {
        fun onRefresh(refreshLayout: IViewRefreshLayout)
    }

}