package com.ljwx.baseapp.vm

abstract class DataRequestCallback<D> {

    abstract fun onSuccess(data: D)

    open fun onError(code: Int, message: String?) {

    }

}