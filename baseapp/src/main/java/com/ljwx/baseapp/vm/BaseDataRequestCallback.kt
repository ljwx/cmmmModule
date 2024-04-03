package com.ljwx.baseapp.vm

abstract class BaseDataRequestCallback<D> {

    abstract fun onSuccess(data: D?)

    open fun onFail(code: Int?, message: String? = null, data: Any? = null) {

    }

    open fun onError(code: Int?, message: String? = null, exception: Throwable? = null) {

    }

}