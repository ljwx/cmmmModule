package com.ljwx.baseapp.vm.model

interface IQuickObserver<T : Any> {

    fun onResponse(response: T)

    fun <D> onResponseSuccess(data: D)

    fun onResponseFail(response: T)

    fun onErrorGlobal(e: Throwable)

    fun onResponseFailGlobal(response: T)

}