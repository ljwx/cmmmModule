package com.ljwx.baseapp.vm

import java.lang.Exception

abstract class BaseDataRequestCallback<D> {

    abstract fun onSuccess(data: D?)

    open fun onFail(code: Int?, message: String? = null, data: Any? = null) {

    }

    open fun onError(code: Int?, message: String? = null, exception: Exception? = null) {

    }

}