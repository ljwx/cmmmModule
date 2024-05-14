package com.ljwx.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.util.OtherUtils

abstract class BaseBindingPadFragment<Binding : ViewDataBinding, BindingPad : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    @LayoutRes private val layoutResPad: Int
) : BaseStateRefreshFragment() {

    /**
     * DataBinding
     */
    protected lateinit var mBinding: Binding
    protected lateinit var mBindingPad: BindingPad

    protected var isPad = OtherUtils.isDevicePad()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (isPad) {
            mBindingPad = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        } else {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        }
        quickLayout()
        return if (isPad) mBindingPad.root else mBinding.root
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