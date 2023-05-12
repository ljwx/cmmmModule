package com.ljwx.baseapp.page

interface IPageBroadcast {

    fun registerFinishBroadcast(vararg actions: String?)

    fun registerRefreshBroadcast(vararg actions: String?)

    fun unregisterBroadcast(action: String?)

    fun sendFinishBroadcast(action: String?)

    fun sendRefreshBroadcast(action: String?, type: String? = null)

    fun onPageFinish()

    fun onPageRefresh(type: String?)
}