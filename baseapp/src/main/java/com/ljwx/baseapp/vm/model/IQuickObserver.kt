package com.ljwx.baseapp.vm.model

interface IQuickObserver<T : Any> {

    fun onResponse(response: T)

    fun onResponseSuccess(response: T)

    fun onResponseFail(response: T)

    fun onErrorGlobal(e: Throwable)

    fun onResponseFailGlobal(response: T)

}