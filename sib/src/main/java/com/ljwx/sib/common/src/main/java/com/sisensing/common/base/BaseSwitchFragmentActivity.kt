package com.sisensing.common.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.launcher.ARouter
import com.sisensing.base.BaseViewModel

abstract class BaseSwitchFragmentActivity<V : ViewDataBinding, VM : BaseViewModel<*>> :
    BaseActivity<V, VM>() {

    private var fragmentTransaction: FragmentTransaction? = null

    /**
     * showFragment
     *
     * @param layout
     * @param tag
     */
    open fun showFragment(layout: Int, tag: String) {
        val f = getFragment(tag)
        if (f != null) {
            ensureTransaction()
            if (!f.isAdded) {
                fragmentTransaction?.add(layout, f, tag)
            }
            fragmentTransaction?.show(f)
            fragmentTransaction?.setMaxLifecycle(f, Lifecycle.State.RESUMED)
        }
        commitTransactions(tag)
    }

    /**
     * 隐藏fragment
     *
     * @param tag
     */
    open fun hideFragment(tag: String?) {
        val f = getFragment(tag)
        if (f != null && !f.isDetached) {
            ensureTransaction()
            fragmentTransaction?.hide(f)
            fragmentTransaction?.setMaxLifecycle(f, Lifecycle.State.STARTED)
        }
    }

    open fun getFragment(tag: String?): Fragment? {
        var f = supportFragmentManager.findFragmentByTag(tag)
        if (f == null) {
            f = ARouter.getInstance().build(tag).navigation() as Fragment
        }
        return f
    }

    open fun commitTransactions(tag: String) {
        if (fragmentTransaction?.isEmpty == false) {
            fragmentTransaction?.commitAllowingStateLoss()
            fragmentTransaction = null
        }
    }

    private fun ensureTransaction(): FragmentTransaction? {
        if (fragmentTransaction == null) {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        return fragmentTransaction
    }

}