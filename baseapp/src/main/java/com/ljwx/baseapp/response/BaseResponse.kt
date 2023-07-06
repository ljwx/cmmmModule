package com.ljwx.baseapp.response

sealed class BaseResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val exception: ResponseException) : BaseResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

}