package com.ljwx.baseapp.vm.model

import com.ljwx.baseapp.response.DataResult

interface IQuickObserver<T : Any> {

    fun onResponse(response: T)

    fun onResponseSuccess(dataResult: DataResult.Success<T>)

    fun onResponseFail(dataResult: DataResult.Fail<T>)

}