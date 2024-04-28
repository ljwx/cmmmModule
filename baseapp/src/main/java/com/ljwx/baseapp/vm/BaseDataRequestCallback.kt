package com.ljwx.baseapp.vm

interface BaseDataRequestCallback<D> {

    fun onSuccess(data: D?)

    open fun onFail(code: Int? = null, message: String? = null, data: Any? = null)

    open fun onError(code: Int? = null, message: String? = null, exception: Throwable? = null)

}