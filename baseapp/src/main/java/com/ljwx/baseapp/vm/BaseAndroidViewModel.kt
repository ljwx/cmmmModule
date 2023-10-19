package com.ljwx.baseapp.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ljwx.baseapp.response.DataResult
import com.ljwx.baseapp.vm.model.BaseDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseAndroidViewModel<M : BaseDataRepository<*>>(application: Application) :
    IBaseViewModel<M>, AndroidViewModel(application), DefaultLifecycleObserver {

    open val TAG = this.javaClass.simpleName

    protected var mRepository: M

    private val mShowPopLoading = MutableLiveData<Triple<Boolean, Int, String>>()
    private val mDismissPopLoading = MutableLiveData<Triple<Boolean, Int, String>>()
    val popLoadingShow: MutableLiveData<Triple<Boolean, Int, String>>
        get() = mShowPopLoading
    val popLoadingDismiss: MutableLiveData<Triple<Boolean, Int, String>>
        get() = mDismissPopLoading

    init {
        mRepository = createRepository()
    }

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
        mDismissPopLoading.postValue(Triple(dismiss, code ?: 0, message ?: ""))
    }

    override fun commonResponseNotSuccess(result: DataResult<*>) {

    }

}