package com.ljwx.basefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes private val layoutResID: Int) : Fragment() {

    open val TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(layoutResID, container, false)
    }

}