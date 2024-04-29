package com.ljwx.baseapp.event

interface ISendLocalEvent {

    fun sendLocalEvent(action: String?, type: Int? = null)

}