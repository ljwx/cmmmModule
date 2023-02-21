package com.ljwx.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.BaseViewModel
import java.lang.reflect.ParameterizedType

open abstract class BaseMVVMFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel> :
    BaseStateRefreshFragment() {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // 设置DataBinding
        mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        fastLayout()
        // 创建ViewModel
        val type = javaClass.genericSuperclass as ParameterizedType
        val modelClass = type.actualTypeArguments.getOrNull(1) as Class<ViewModel>
        mViewModel = ViewModelProvider(this)[modelClass]

        return mBinding.root
    }

    /**
     * 快速布局
     */
    private fun fastLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

}