package com.ljwx.baseactivity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.page.IPageViewModel
import com.ljwx.baseapp.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

open class BaseMVVMActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>>(@LayoutRes private val layoutResID: Int) :
    BaseBindingActivity<Binding>(layoutResID) {

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
        return ViewModelProvider(this)[modelClass]
    }

}