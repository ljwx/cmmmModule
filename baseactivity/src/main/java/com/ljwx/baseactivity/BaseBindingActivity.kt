package com.ljwx.baseactivity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.util.BaseModuleLog

open class BaseBindingActivity<Binding : ViewDataBinding>(@LayoutRes private val layoutResID: Int) :
    BaseStateRefreshActivity() {

    protected lateinit var mBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResID)
        //等待binding过后
        initToolbar(com.ljwx.baseapp.R.id.base_app_toolbar)
        quickLayout()
    }


    protected fun quickLayout() {
        BaseModuleLog.d(TAG, "静默初始化多状态和刷新")
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

}