package com.ljwx.baseapp.page

interface IPageBroadcast {

    fun registerCommonBroadcast(action: String?)
//    fun registerFinishBroadcast(vararg actions: String?)
//
//    fun registerRefreshBroadcast(vararg actions: String?)
//
//    fun registerOtherBroadcast(action: String)

    fun unregisterBroadcast(action: String?)

    fun sendLocalBroadcast(action: String?)

//    fun sendFinishBroadcast(action: String?)
//
//    fun sendRefreshBroadcast(action: String?, params: String? = null)
//
//    fun sendOtherBroadcast(action: String)

//    fun onBroadcastPageFinish()

//    fun onBroadcastPageRefresh(type: String?)

    fun onCommonBroadcast(action: String)

}