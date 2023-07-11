package com.ljwx.basedialog

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseBindingDialogFragment<Binding : ViewDataBinding> : DialogFragment() {

    protected lateinit var mBinding: Binding
    protected lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val width = dm.widthPixels * 1
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, caonima(), container, false)
        mBinding.lifecycleOwner = this
        // 隐藏标题栏, 不加弹窗上方会一个透明的标题栏占着空间
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        //        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return mBinding.root
    }

    abstract fun caonima(): Int

    fun isShowing(): Boolean {
        return dialog?.isShowing == true
    }

    protected fun setGravity(gravity: Int) {
        val attr = dialog?.window?.attributes
        attr?.gravity = gravity
        dialog?.window?.attributes = attr
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

//    open fun show(manager: FragmentManager){
//        val tag = this.javaClass.simpleName
//        Log.d("ljwx2", tag)
//        show(manager, tag)
//    }

}