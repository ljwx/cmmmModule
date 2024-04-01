package com.ljwx.basefragment

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.ViewModelScope
import java.lang.reflect.ParameterizedType

abstract class BaseVMFragment<ViewModel : BaseViewModel<*>> :
    BaseStateRefreshFragment() {

    private val mViewModelScope by lazy {
        ViewModelScope()
    }

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel)
        initPopLoadingObserver()
    }

    open fun initPopLoadingObserver() {
        mViewModel.popLoadingShow.observe(this) {
            showPopLoading(it.first)
        }
        mViewModel.popLoadingDismiss.observe(this) {
            dismissPopLoading(it.first)
        }
    }

    open fun createViewModel(): ViewModel {
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments.getOrNull(1) as Class<ViewModel>
        return if (useActivityScopeVM()) mViewModelScope.getActivityScopeViewModel(
            requireActivity(),
            modelClass
        ) else ViewModelProvider(this)[modelClass]
    }

    open fun useActivityScopeVM() = false

    protected fun <L : LiveData<T>, T> L.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }

    override fun observeData() {
        mViewModel.finishActivity.observe(this) {
            if (it) {
                activity?.finish()
            }
        }
        mViewModel.scope()
    }

    open fun ViewModel.scope() {

    }

}