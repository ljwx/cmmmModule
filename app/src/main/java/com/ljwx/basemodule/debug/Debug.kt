package com.ljwx.basemodule.debug

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

fun BaseActivity.delayRun(time: Long, block: () -> Unit) {
    delayRunCommon(lifecycleScope, time, block)
}

fun BaseFragment.delayRun(time: Long, block: () -> Unit) {
    delayRunCommon(lifecycleScope, time, block)
}

fun BaseViewModel<*>.delayRun(time: Long, block: () -> Unit) {
    delayRunCommon(viewModelScope, time, block)
}

private fun delayRunCommon(lifecycleScope: CoroutineScope, time: Long, block: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        delay(time)
        withContext(Dispatchers.Main) {
            block
        }
    }
}