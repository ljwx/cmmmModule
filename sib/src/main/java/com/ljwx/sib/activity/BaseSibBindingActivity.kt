package com.ljwx.sib.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.sib.BR

abstract class BaseSibBindingActivity<Binding : ViewDataBinding>() :
    BaseSibStateRefreshActivity() {

    protected lateinit var mBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userNewBaseActivityLogic) {
            newBaseActivityCreate()
        }
        quickLayout()
    }

    abstract fun getLayoutId(): Int

    open fun newBaseActivityCreate() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        //等待binding过后
        initToolbar(com.ljwx.baseapp.R.id.base_app_toolbar)
    }

    protected fun quickLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (userNewBaseActivityLogic) {
            mBinding.unbind()
        }
    }

}