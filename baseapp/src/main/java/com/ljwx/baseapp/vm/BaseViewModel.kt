package com.ljwx.baseapp.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<R : BaseDataRepository> : ViewModel(), DefaultLifecycleObserver {

    protected var mRepository: R

    private val mShowPopLoading = MutableLiveData<Triple<Boolean, Int, String>>()
    val popLoading: MutableLiveData<Triple<Boolean, Int, String>>
        get() = mShowPopLoading

    init {
        mRepository = createRepository()
    }

    abstract fun createRepository(): R

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mRepository.onCleared()
    }

    open fun showPopLoading(show: Boolean = true, code: Int? = 0, message: String? = "") {
        mShowPopLoading.postValue(Triple(show, code ?: 0, message ?: ""))
    }

    open fun dismissPopLoading(dismiss: Boolean = true, code: Int? = 0, message: String? = "") {
        mShowPopLoading.postValue(Triple(dismiss, code ?: 0, message ?: ""))
    }

}