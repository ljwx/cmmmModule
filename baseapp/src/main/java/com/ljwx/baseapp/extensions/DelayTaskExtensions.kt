package com.ljwx.baseapp.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

inline fun AppCompatActivity.delayRun(time: Long = 2000, crossinline block: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        delay(time)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

inline fun Fragment.delayRun(time: Long = 2000, crossinline block: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        delay(time)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

inline fun ViewModel.delayRun(time: Long = 2000, crossinline block: () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        delay(time)
        withContext(Dispatchers.Main) {
            block()
        }
    }
}

inline fun AppCompatActivity.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    crossinline condition: (() -> Boolean) = { true },
    crossinline block: (times: Int) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        for (i in 0 until times) {
            delay(delayTime)
            if (condition()) {
                withContext(Dispatchers.Main) {
                    block(i)
                }
            }
        }
    }
}

inline fun Fragment.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    crossinline condition: (() -> Boolean) = { true },
    crossinline block: (times: Int) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        for (i in 0 until times) {
            delay(delayTime)
            if (condition()) {
                withContext(Dispatchers.Main) {
                    block(i)
                }
            }
        }
    }
}

inline fun ViewModel.intervalDelay(
    times: Int = 15,
    delayTime: Long = 2000,
    crossinline condition: (() -> Boolean) = { true },
    crossinline block: (times: Int) -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        for (i in 0 until times) {
            delay(delayTime)
            if (condition()) {
                withContext(Dispatchers.Main) {
                    block(i)
                }
            }
        }
    }
}