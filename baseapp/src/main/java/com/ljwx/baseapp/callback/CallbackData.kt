package com.ljwx.baseapp.callback

interface CallbackData {

    fun <T> invoke(data: T)

}