package com.ljwx.baseapp.business

interface BaseRefreshLayout {

    fun setRefreshHeader()

    fun setOnRefreshListener(refreshListener: RefreshListener)

    fun refreshFinish()

    interface RefreshListener {
        fun onRefresh(refreshLayout: BaseRefreshLayout)
    }

}