package com.ljwx.baseapp.page

interface IPageBroadcast {

    fun registerFinishBroadcast(vararg actions: String?)

    fun registerRefreshBroadcast(vararg actions: String?)

    fun registerOtherBroadcast(action: String)

    fun unregisterBroadcast(action: String?)

    fun sendFinishBroadcast(action: String?)

    fun sendRefreshBroadcast(action: String?, params: String? = null)

    fun sendOtherBroadcast(action: String)

    fun onBroadcastPageFinish()

    fun onBroadcastPageRefresh(type: String?)

    fun onBroadcastOther(action: String?)
}