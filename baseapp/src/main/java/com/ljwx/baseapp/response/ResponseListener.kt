package com.ljwx.baseapp.response

interface ResponseListener<T> {

    fun onResponse(response: BaseResponse<T>?)

}