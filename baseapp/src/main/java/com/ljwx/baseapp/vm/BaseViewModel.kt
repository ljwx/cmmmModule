package com.ljwx.baseapp.vm

import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.StringUtils

abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    private val mShowPopLoading = MutableLiveData<Triple<Boolean, Int, String>>()
    val popLoading: MutableLiveData<Triple<Boolean, Int, String>>
        get() = mShowPopLoading

    init {
        initRepository()
    }

    abstract fun initRepository()

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    open fun showPopLoading(show: Boolean = true, code: Int? = 0, message: String? = "") {
        mShowPopLoading.postValue(Triple(show, code ?: 0, message ?: ""))
    }

    open fun dismissPopLoading(dismiss: Boolean = true, code: Int? = 0, message: String? = "") {
        mShowPopLoading.postValue(Triple(dismiss, code ?: 0, message ?: ""))
    }

    open fun getString(@StringRes id: Int) :String{
        return StringUtils.getString(id)
    }

}