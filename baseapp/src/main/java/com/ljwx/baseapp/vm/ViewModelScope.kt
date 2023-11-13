package com.ljwx.baseapp.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ljwx.baseapp.util.BaseAppUtils

class ViewModelScope {

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    fun <T : ViewModel> getFragmentScopeViewModel(fragment: Fragment, modelClass: Class<T>): T {
        if (mFragmentProvider == null) mFragmentProvider = ViewModelProvider(fragment)
        return mFragmentProvider!!.get<T>(modelClass)
    }

    fun <T : ViewModel> getActivityScopeViewModel(
        activity: FragmentActivity,
        modelClass: Class<T>
    ): T {
        if (mActivityProvider == null) mActivityProvider = ViewModelProvider(activity)
        return mActivityProvider!!.get<T>(modelClass)
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) mActivityProvider = ViewModelProvider(BaseAppUtils.getApplicationViewModelStore())
        return mApplicationProvider!!.get<T>(modelClass)
    }

}