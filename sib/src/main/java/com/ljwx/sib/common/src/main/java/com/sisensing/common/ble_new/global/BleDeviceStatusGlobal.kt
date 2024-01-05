package com.sisensing.common.ble_new.global

object BleDeviceStatusGlobal {

    private val connectStatus: BleDeviceStatusLiveData<Pair<Int, Boolean>> =
        BleDeviceStatusLiveData()

    private val observerContainer = HashMap<String, BleDeviceGlobalObserver>()

    @JvmStatic
    fun addConnectStatusObserver(tag: String, observer: BleDeviceGlobalObserver) {
        observerContainer[tag] = observer
        connectStatus.observeForever(observer)
    }

    @JvmStatic
    fun removeConnectStatusObserver(tag: String) {
        val observer = observerContainer[tag]
        if (observer != null) {
            connectStatus.removeObserver(observer)
            observerContainer.remove(tag)
        }
    }

    @JvmStatic
    fun connectStatusChange(): BleDeviceStatusLiveData<Pair<Int, Boolean>> {
        return connectStatus
    }

    @JvmStatic
    fun changeConnectStatus(status: Int, showToast: Boolean = true) {
        connectStatus.postValue(Pair(status, showToast))
    }

}