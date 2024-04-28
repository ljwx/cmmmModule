package com.ljwx.baseapp.response

sealed class DataResult<out R> {

    data class Success<out T>(val data: T) : DataResult<T>()
    data class Fail<out T>(
        val code: Int? = null,
        val message: String? = null,
        val data: T? = null
    ) : DataResult<T>()

    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val exception: Throwable? = null
    ) : DataResult<Nothing>()

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