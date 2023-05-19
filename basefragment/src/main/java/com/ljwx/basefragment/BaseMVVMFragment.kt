package com.ljwx.baseactivity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.BaseViewModel
import java.lang.reflect.ParameterizedType

open class BaseMVVMActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel>(@LayoutRes val layoutResID: Int) :
    StateRefreshActivity() {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResID)
        initViewModel()
        lifecycle.addObserver(mViewModel)
        quickLayout()
    }

    private fun initViewModel() {
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments.getOrNull(1) as Class<ViewModel>
        mViewModel = ViewModelProvider(this)[modelClass]
    }

    /**
     * 快速布局
     */
    protected fun quickLayout() {
        initToolbar()
        useCommonStateLayout()
        useCommonRefreshLayout()
    }


}