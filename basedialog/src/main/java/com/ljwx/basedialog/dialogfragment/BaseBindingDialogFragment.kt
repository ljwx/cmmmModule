package com.ljwx.basedialog.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialogFragment<Binding : ViewDataBinding>() : BaseDialogFragment() {

    protected lateinit var mBinding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        mBinding.lifecycleOwner = this
        initWidth()
        return mBinding.root
    }

    abstract fun layoutId(): Int

    protected fun setGravity(gravity: Int) {
        val attr = dialog?.window?.attributes
        attr?.gravity = gravity
        dialog?.window?.attributes = attr
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }



}