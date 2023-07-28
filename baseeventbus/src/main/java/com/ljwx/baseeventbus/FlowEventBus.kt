package com.ljwx.baseeventbus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

object FlowEventBus {

    //用HashMap存储SharedFlow
    private val flowEvents = ConcurrentHashMap<String, MutableSharedFlow<Any>>()

    //获取Flow，当相应Flow不存在时创建
    fun getFlow(key: String): MutableSharedFlow<Any> {
        return flowEvents[key] ?: MutableSharedFlow<Any>().also { flowEvents[key] = it }
    }

    fun post(event: Any, delay: Long = 0) {
        MainScope().launch {
            delay(delay)
            getFlow(event.javaClass.simpleName).emit(event)
        }
    }


    //做了一点改造，加了Lifecycle.State参数可以更精细地将控制接受到事件时的执行时机
    inline fun <reified T : Any> observe(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        crossinline onReceived: (T) -> Unit
    ) = lifecycleOwner.lifecycleScope.launch(dispatcher) {
        getFlow(T::class.java.simpleName).collect {
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                if (it is T) onReceived(it)
            }
        }
    }

}