package com.ljwx.baseapp.page

interface IPageReceiveEvent {

    fun registerEvent(action: String?)

    fun unregisterEvent(action: String?)

    fun sendEvent(action: String?)

    fun onReceiveEvent(action: String)

}