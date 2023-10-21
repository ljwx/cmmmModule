package com.ljwx.baseapp.page

import com.ljwx.baseapp.event.ISendEvent

interface IPageReceiveEvent :ISendEvent{

    fun registerLocalEvent(action: String?)

    fun unregisterLocalEvent(action: String?)

    fun onReceiveLocalEvent(action: String)

}