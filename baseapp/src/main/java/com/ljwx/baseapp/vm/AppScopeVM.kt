package com.ljwx.baseapp.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.util.BaseAppUtils

object AppScopeVM {

    val vmProvider = ViewModelProvider(BaseAppUtils.getApplicationViewModelStore())

    inline fun <reified T : ViewModel> get(): T {
        val vm = vmProvider.get(T::class.java)
        return vm
    }

}