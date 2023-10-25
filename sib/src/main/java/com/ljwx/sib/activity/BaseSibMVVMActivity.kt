package com.ljwx.sib.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.vm.BaseAndroidViewModel
import java.lang.reflect.ParameterizedType
import com.ljwx.sib.BR

abstract class BaseSibMVVMActivity<Binding : ViewDataBinding, ViewModel : BaseAndroidViewModel<*>> :
    BaseSibBindingActivity<Binding>() {

    protected lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun newBaseActivityCreate() {
        super.newBaseActivityCreate()
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel)
        //关联ViewModel
        mBinding.setVariable(BR.pageMainViewModel, mViewModel)
        //loading
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
        return ViewModelProvider(this)[modelClass]
    }

    protected fun <L : LiveData<T>, T> L.observe(observer: Observer<T>) {
        observe(this@BaseSibMVVMActivity, observer)
    }

    override fun observeData() {
        if (userNewBaseActivityLogic) {
            mViewModel.scope()
        }
    }

    open fun ViewModel.scope() {

    }

}