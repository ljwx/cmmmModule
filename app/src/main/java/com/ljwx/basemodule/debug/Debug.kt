package com.ljwx.basemodule.debug

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.ljwx.baseactivity.BaseActivity
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basefragment.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun BaseActivity.delayRun(time: Long = 2000, block: () -> Unit) {
    delayRunCommon(lifecycleScope, time, block)
}

fun BaseFragment.delayRun(time: Long = 2000, block: () -> Unit) {
    delayRunCommon(lifecycleScope, time, block)
}

fun BaseViewModel<*>.delayRun(time: Long = 2000, block: () -> Unit) {
    delayRunCommon(viewModelScope, time, block)
}

fun BaseActivity.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    condition: (() -> Boolean)? = null,
    block: (times:Int) -> Unit
) {
    intervalDelayCommon(lifecycleScope, times, delayTime, condition, block)
}

fun BaseFragment.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    condition: (() -> Boolean)? = null,
    block: (times:Int) -> Unit
) {
    intervalDelayCommon(lifecycleScope, times, delayTime, condition, block)
}

fun BaseViewModel<*>.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    condition: () -> Boolean,
    block: (times:Int) -> Unit
) {
    intervalDelayCommon(viewModelScope, times, delayTime, condition, block)
}

private fun delayRunCommon(lifecycleScope: CoroutineScope, time: Long, block: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        delay(time)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

private fun intervalDelayCommon(
    lifecycleScope: CoroutineScope,
    times: Int = 15,
    delayTime: Long = 2000,
    condition: (() -> Boolean)? = null,
    block: (times: Int) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        for (i in 0 until times) {
            delay(delayTime)
            if (condition == null || condition()) {
                withContext(Dispatchers.Main) {
                    block(i)
                }
            }
        }
    }
}