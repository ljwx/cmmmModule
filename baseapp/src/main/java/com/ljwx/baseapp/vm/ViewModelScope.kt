package com.ljwx.baseapp.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.constant.BaseLogTag
import com.ljwx.baseapp.util.BaseAppUtils
import com.ljwx.baseapp.util.BaseModuleLog

class ViewModelScope {

    private val TAG = this.javaClass.simpleName + BaseLogTag.VIEW_MODEL_SCOPE

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null

    fun <T : ViewModel> getFragmentScopeViewModel(fragment: Fragment, modelClass: Class<T>): T {
        if (mFragmentProvider == null) mFragmentProvider = ViewModelProvider(fragment)
        BaseModuleLog.d(TAG, "从fragment(${fragment.javaClass.simpleName})获取viewmodel")
        return mFragmentProvider!!.get<T>(modelClass)
    }

    fun <T : ViewModel> getActivityScopeViewModel(
        activity: FragmentActivity,
        modelClass: Class<T>
    ): T {
        if (mActivityProvider == null) mActivityProvider = ViewModelProvider(activity)
        BaseModuleLog.d(TAG, "从activity(${activity.javaClass.simpleName})获取viewmodel")
        return mActivityProvider!!.get<T>(modelClass)
    }

}