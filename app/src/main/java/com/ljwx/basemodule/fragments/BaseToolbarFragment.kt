package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ljwx.basefragment.BaseStateRefreshFragment
import com.ljwx.basemodule.R

class BaseToolbarFragment :
    BaseStateRefreshFragment(R.layout.fragment_base_toolbar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun lazyInit() {
        super.lazyInit()
        Log.d("ljwx2", "第一个可见")
    }
}