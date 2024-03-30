package com.ljwx.baseapp.vm

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ljwx.baseapp.infochange.IBaseConfigInfo
import com.ljwx.baseapp.infochange.IBaseUserInfo

object GlobalDataRepository {

    open val TAG = this.javaClass.simpleName + "-[Global"

    private val userInfo = MutableLiveData<IBaseUserInfo?>()
    private val configInfo = MutableLiveData<IBaseConfigInfo?>()

    fun observeUserInfo(owner: LifecycleOwner, observer: Observer<IBaseUserInfo?>) {
        Log.d(TAG, "添加观察者")
        userInfo.observe(owner, observer)
    }

    fun setUserInfo(info: IBaseUserInfo?, changeType: Int? = 0) {
        info?.setInfoChangeType(changeType)
        userInfo.value = info
    }

    fun postUserInfo(info: IBaseUserInfo?, changeType: Int? = 0) {
        info?.setInfoChangeType(changeType)
        userInfo.postValue(info)
    }

    fun observeConfigInfo(owner: LifecycleOwner, observer: Observer<IBaseConfigInfo?>) {
        configInfo.observe(owner, observer)
    }

    fun setConfigInfo(info: IBaseConfigInfo?, changeType: Int? = 0) {
        info?.setInfoChangeType(changeType)
        configInfo.value = info
    }

    fun postConfigInfo(info: IBaseConfigInfo?, changeType: Int? = 0) {
        info?.setInfoChangeType(changeType)
        configInfo.postValue(info)
    }

}