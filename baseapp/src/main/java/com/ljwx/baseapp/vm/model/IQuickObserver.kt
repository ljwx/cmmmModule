package com.ljwx.baseapp.vm.model

interface IQuickObserver<T : Any> {

    fun onResponse(response: T)

    fun onResponseSuccess(dataResult: T)

    fun onResponseFail(dataResult: T)

    fun onErrorGlobal(e: Throwable)

    fun onResponseFailGlobal(response: T)

}