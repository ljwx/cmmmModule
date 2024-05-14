package com.ljwx.baseactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.extensions.getStringRes
import com.ljwx.baseapp.util.OtherUtils

abstract class BaseBindingPadActivity<Binding : ViewDataBinding, BindingPad : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    @LayoutRes private val layoutResPad: Int
) : BaseStateRefreshActivity() {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding
    protected lateinit var mBindingPad: BindingPad

    protected var isPad = OtherUtils.isDevicePad()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPad) {
            mBindingPad = DataBindingUtil.setContentView(this, getLayoutRes())
        } else {
            mBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        }
        //等待binding过后
        initToolbar(com.ljwx.baseapp.R.id.base_app_toolbar)
        quickLayout()
    }

    override fun getLayoutRes(): Int {
        return if (isPad) layoutResPad else layoutRes
    }

    private fun quickLayout() {
        useCommonStateLayout()
        useCommonRefreshLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPad) {
            mBindingPad.unbind()
        } else {
            mBinding.unbind()
        }
    }

}