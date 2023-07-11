package com.ljwx.baseactivity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseBindingActivity<Binding : ViewDataBinding>(@LayoutRes private val layoutResID: Int) :
    BaseStateRefreshActivity() {

    protected lateinit var mBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResID)
        initToolbar(com.ljwx.baseapp.R.id.base_app_toolbar)
        quickLayout()
    }


    protected fun quickLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

}