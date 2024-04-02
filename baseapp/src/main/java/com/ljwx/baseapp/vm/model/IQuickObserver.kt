package com.ljwx.baseapp.vm.model

interface IQuickObserver<T> {

    fun onResponse(response: T)

    fun onResponseSuccess(response: T)

    fun onResponseFail(code: Int?, message: String? = null, data: T? = null)

    fun onResponseFailGlobal(code: Int?, message: String?)

    fun onErrorGlobal(code: Int?, e: Throwable?)

}