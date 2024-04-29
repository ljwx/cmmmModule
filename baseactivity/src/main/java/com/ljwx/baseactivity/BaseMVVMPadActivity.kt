package com.ljwx.baseactivity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.util.BaseModuleLog
import com.ljwx.baseapp.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseMVVMPadActivity<Binding : ViewDataBinding, BindingPad : ViewDataBinding, ViewModel : BaseViewModel<*>>(
    @LayoutRes layoutRes: Int,
    @LayoutRes private val layoutResPad: Int
) : BaseBindingPadActivity<Binding, BindingPad>(layoutRes, layoutResPad) {

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
        val modelClass = type.actualTypeArguments.getOrNull(2) as Class<ViewModel>
        BaseModuleLog.d(TAG, "创建viewmodel")
        return ViewModelProvider(this)[modelClass]
    }

    protected fun <L : LiveData<T>, T> L.observe(observer: Observer<T>) {
        observe(this@BaseMVVMPadActivity, observer)
    }

    override fun observeData() {
        mViewModel.finishActivity.observe(this) {
            if (it && !isFinishing) {
                finish()
            }
        }
        mViewModel.scope()
    }

    open fun ViewModel.scope() {

    }

}