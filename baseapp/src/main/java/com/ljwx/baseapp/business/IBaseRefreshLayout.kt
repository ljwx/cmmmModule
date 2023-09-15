package com.ljwx.baseapp.business

interface IBaseRefreshLayout {

    fun setRefreshHeader(header: IBaseRefreshHeader)

    fun setOnRefreshListener(refreshListener: RefreshListener)

    fun refreshFinish()

    interface RefreshListener {
        fun onRefresh(refreshLayout: IBaseRefreshLayout)
    }

}