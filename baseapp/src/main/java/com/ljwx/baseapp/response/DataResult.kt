package com.ljwx.baseapp.response

sealed class DataResult<out R : Any> {

    data class Success<out T : Any>(val data: T) : DataResult<T>()
    data class Fail<out T : Any>(val data: T) : DataResult<T>()
    data class Error(val exception: Exception) : DataResult<Nothing>()

    interface Result<D : Any> {
        fun onResult(result: DataResult<D>)
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Fail<*> -> "Fail[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

}

val DataResult<*>.succeeded
    get() = this is DataResult.Success && data != null