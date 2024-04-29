package com.ljwx.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.util.BaseModuleLog

abstract class BaseBindingFragment<Binding : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    BaseStateRefreshFragment(layoutRes) {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        quickLayout()
        return mBinding.root
    }

    private fun quickLayout() {
        BaseModuleLog.d(TAG, "静默初始化多状态和刷新")
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

}