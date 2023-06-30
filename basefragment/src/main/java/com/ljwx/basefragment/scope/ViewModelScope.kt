package com.ljwx.basefragment.scope

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelScope {

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null

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

}