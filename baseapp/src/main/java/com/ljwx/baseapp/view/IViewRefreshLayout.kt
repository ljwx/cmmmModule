package com.ljwx.baseapp.view

import android.view.ViewGroup

interface IViewRefreshLayout {

    fun setRefreshHeader(header: IViewRefreshHeader)

    fun setOnRefreshListener(refreshListener: RefreshListener)

    fun refreshFinish()

    fun getView(): ViewGroup
    interface RefreshListener {
        fun onRefresh(refreshLayout: IViewRefreshLayout)
    }

}