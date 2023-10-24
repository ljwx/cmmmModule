package com.ljwx.sib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseSibBindingFragment<Binding : ViewDataBinding>() :
    BaseSibStateRefreshFragment() {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quickLayout()
    }

    private fun quickLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

}