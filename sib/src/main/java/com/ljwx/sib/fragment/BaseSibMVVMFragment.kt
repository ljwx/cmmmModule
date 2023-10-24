package com.ljwx.sib.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.vm.BaseAndroidViewModel
import com.ljwx.baseapp.vm.ViewModelScope
import java.lang.reflect.ParameterizedType

abstract class BaseSibMVVMFragment<Binding : ViewDataBinding, ViewModel : BaseAndroidViewModel<*>>(@LayoutRes layoutRes: Int) :
    BaseSibBindingFragment<Binding>() {

    private val mViewModelScope by lazy {
        ViewModelScope()
    }

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userNewBaseFragmentLogic) {
            mViewModel = createViewModel()
            lifecycle.addObserver(mViewModel)
            initPopLoadingObserver()
        }
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
        mViewModel.scope()
    }

    open fun ViewModel.scope() {

    }

}