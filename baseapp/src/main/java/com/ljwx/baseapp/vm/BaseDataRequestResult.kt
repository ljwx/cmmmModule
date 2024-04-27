package com.ljwx.baseapp.vm

abstract class BaseDataRequestResult<D> : BaseDataRequestCallback<D> {

    override fun onFail(code: Int?, message: String?, data: Any?) {

    }

    override fun onError(code: Int?, message: String?, exception: Throwable?) {

    }

}