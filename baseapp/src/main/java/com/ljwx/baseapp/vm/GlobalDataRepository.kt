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

    fun <T : IBaseUserInfo> setUserInfo(info: T, changeType: Int? = 0) {
        info.setInfoChangeType(changeType)
        userInfo.value = info
    }

    fun <T : IBaseUserInfo> postUserInfo(info: T) {
        userInfo.postValue(info)
    }

    fun observeConfigInfo(owner: LifecycleOwner, observer: Observer<IBaseConfigInfo?>) {
        configInfo.observe(owner, observer)
    }

    fun <T : IBaseConfigInfo> setConfigInfo(info: T, changeType: Int? = 0) {
        info.setInfoChangeType(changeType)
        configInfo.value = info
    }

    fun <T : IBaseConfigInfo> postUserInfo(info: T) {
        configInfo.postValue(info)
    }

}