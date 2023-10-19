package com.ljwx.baseapp.coroutine

import kotlinx.coroutines.CoroutineScope

interface ICoroutineQuick {

    fun threadRun(
        child: suspend CoroutineScope.() -> Unit,
        main: suspend CoroutineScope.() -> Unit
    )

    fun threadRun(
        childDelay: Long,
        main: suspend CoroutineScope.() -> Unit
    )

}