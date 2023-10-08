package com.ljwx.basedialog.dialogfragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.ViewModelScope
import java.lang.reflect.ParameterizedType

abstract class BaseMVVMDialogFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>> :
    BaseBindingDialogFragment<Binding>() {

    protected lateinit var mViewModel: ViewModel

    private val mViewModelScope by lazy {
        ViewModelScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel)
    }

    open fun createViewModel(): ViewModel {
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments.getOrNull(1) as Class<ViewModel>
        return if (useActivityScopeVM()) mViewModelScope.getActivityScopeViewModel(
            mActivity,
            modelClass
        ) else ViewModelProvider(this)[modelClass]
    }

    open fun useActivityScopeVM() = false

}