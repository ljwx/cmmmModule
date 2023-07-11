package com.ljwx.baseapp.response

open class ResponseException @JvmOverloads constructor(
    private val throwable: Throwable,
    val customCode: Int? = null,
    val customMessage: String? = null,
) : Exception(throwable)