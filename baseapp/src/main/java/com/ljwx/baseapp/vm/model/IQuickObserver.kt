package com.ljwx.baseapp.vm.model

interface IQuickObserver<T : Any> {

    fun onResponse(response: T)

    fun onResponseSuccess(response: T)

    fun onResponseFail(code: Int?, message: String?)

    fun onErrorGlobal(e: Throwable)

    fun onResponseFailGlobal(code: Int?, message: String?)

}