package com.ljwx.baseapp.vm

import java.lang.Exception

abstract class DataRequestCallback<D> {

    abstract fun onSuccess(data: D?)

    open fun onFail(code: Int?, message: String?, data: D?) {

    }

    open fun onError(code: Int?, message: String?, exception: Exception?) {

    }

}