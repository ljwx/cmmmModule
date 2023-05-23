package com.ljwx.baseapp.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() ,DefaultLifecycleObserver{

    init {
        initRepository()
    }

    abstract fun initRepository()

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

}