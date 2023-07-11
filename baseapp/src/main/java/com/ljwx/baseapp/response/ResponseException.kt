package com.ljwx.baseapp.response

open class ResponseException @JvmOverloads constructor(
    val exceptionCode: Int? = null,
) : Exception()