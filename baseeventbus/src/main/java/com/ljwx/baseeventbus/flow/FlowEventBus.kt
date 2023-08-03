package com.ljwx.baseeventbus.flow

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object FlowEventBus {

    private val busMap by lazy {
        mutableMapOf<String, EventBus<*>>()
    }

    private val busMapStick by lazy {
        mutableMapOf<String, StickEventBus<*>>()
    }

    fun <T> get(key: String): EventBus<T> {
        var event = busMap[key]
        if (event == null) {
            event = EventBus<T>(key)
            busMap[key] = event
        }
        return event as EventBus<T>
    }

    fun <T> getStick(key: String): StickEventBus<T> {
        var event = busMapStick[key]
        if (event == null) {
            event = StickEventBus<T>(key)
            busMapStick[key] = event
        }
        return event as StickEventBus<T>
    }

    open class EventBus<T>(private val key: String) : DefaultLifecycleObserver {

        protected open val _events: MutableSharedFlow<T> by lazy {
            MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }

        fun observe(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                _events.collect {
                    action(it)
                }
            }
        }

        fun observeForever(action: (t: T) -> Unit){
            GlobalScope.launch {
                _events.collect{
                    action(it)
                }
            }
        }

        fun post(owner: LifecycleOwner, event: T) {
            owner.lifecycleScope.launch {
                _events.emit(event)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            Log.d("ljwx2", "flowEventBus走destroy")
            val subscriptCount = _events.subscriptionCount.value
            if (subscriptCount <= 0) {
                busMap.remove(key)
            }
        }

    }

    class StickEventBus<T>(key: String) : EventBus<T>(key) {
        override val _events: MutableSharedFlow<T> by lazy {
            MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
    }


}