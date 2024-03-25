package com.ljwx.baseapp.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ljwx.baseapp.user.IBaseUserInfo

object GlobalDataRepository {

    private val userInfo = MutableLiveData<IBaseUserInfo?>()

    fun observeUserInfo(owner: LifecycleOwner, observer: Observer<IBaseUserInfo?>) {
        userInfo.observe(owner, observer)
    }

    fun <T : IBaseUserInfo> setUserInfo(info: T, changeType: Int? = 0) {
        info.setInfoChangeType(changeType)
        userInfo.value = info
    }

    fun <T : IBaseUserInfo> postUserInfo(info: T) {
        userInfo.postValue(info)
    }

}