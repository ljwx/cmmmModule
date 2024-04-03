package com.ljwx.baseapp.vm

import java.lang.Exception

abstract class BaseDataRequestCallback<D> {

    abstract fun onSuccess(data: D?)

    open fun onFail(code: Int?, message: String?, data: Any?) {

    }

    open fun onError(code: Int?, message: String?, exception: Exception?) {

    }

}