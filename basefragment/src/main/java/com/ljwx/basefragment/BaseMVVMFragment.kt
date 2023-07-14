package com.ljwx.basefragment

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.ViewModelScope
import java.lang.reflect.ParameterizedType

open abstract class BaseMVVMFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>>(@LayoutRes layoutRes: Int) :
    BaseBindingFragment<Binding>(layoutRes) {

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
        mViewModel.popLoading.observe(this) {
            showPopLoading(it.first)
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

}