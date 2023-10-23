package com.ljwx.sib.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseSibBindingActivity<Binding : ViewDataBinding>(@LayoutRes private val layoutResID: Int) :
    BaseSibStateRefreshActivity() {

    protected lateinit var mBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResID)
        //等待binding过后
        initToolbar(com.ljwx.baseapp.R.id.base_app_toolbar)
        quickLayout()
    }


    protected fun quickLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

}