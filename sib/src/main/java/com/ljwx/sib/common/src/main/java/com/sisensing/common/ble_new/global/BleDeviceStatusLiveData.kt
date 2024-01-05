package com.sisensing.common.ble_new.global

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class BleDeviceStatusLiveData<D:Pair<Int, Boolean>> : MutableLiveData<D>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in D>) {
        super.observe(owner, observer)
    }

    fun observeCustom(owner: LifecycleOwner, observer: BleDeviceGlobalObserver) {
        super.observe(owner, observer)
    }

}