package com.ljwx.baseeventbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

object SimpleFlowEventBus {

    //用HashMap存储SharedFlow
    private val flowEvents = ConcurrentHashMap<String, MutableSharedFlow<String>>()

    //获取Flow，当相应Flow不存在时创建
    fun getFlow(key: String): MutableSharedFlow<String> {
        return flowEvents[key] ?: MutableSharedFlow<String>().also { flowEvents[key] = it }
    }

    fun post(key: String, value: String, delay: Long = 0) {
        MainScope().launch {
            delay(delay)
            getFlow(key).emit(value)
        }
    }


    //做了一点改造，加了Lifecycle.State参数可以更精细地将控制接受到事件时的执行时机
    inline fun observe(
        lifecycleOwner: LifecycleOwner,
        key: String,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        crossinline onReceived: (value: String) -> Unit
    ) = lifecycleOwner.lifecycleScope.launch(dispatcher) {
        getFlow(key).collect {
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                onReceived(it)
            }
        }
    }

}