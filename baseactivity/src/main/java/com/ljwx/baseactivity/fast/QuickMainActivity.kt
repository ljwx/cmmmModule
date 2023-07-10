package com.ljwx.baseactivity.fast

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.vm.BaseViewModel

open abstract class QuickMainActivity<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>>(@LayoutRes layoutResID: Int) :
    QuickTabLayoutActivity<Binding, ViewModel>(layoutResID) {

    /**
     * 双击退出提示
     */
    private var mExistTips: String? = null

    /**
     * 双击间隔时间
     */
    private var mExistTimes = 4000

    /**
     * 上次点击时间
     */
    private var mLastClickTime: Long = 0

    /**
     * 设置双击退出
     */
    fun setBackPressExit(tips: String? = null, times: Int = 4000) {
        mExistTips = tips
        mExistTimes = times
    }

    /**
     * 触发返回键
     */
    override fun onBackPressed() {
        if (isFastClick()) {
            super.onBackPressed()
            this.finishAffinity()
            System.exit(0)
            return
        }
        mExistTips?.let {
            
        }
    }

    /**
     * 是否快速点击
     */
    private fun isFastClick(): Boolean {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < 4000) {
            return true
        }
        mLastClickTime = now
        return false
    }

}