package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentHideTestBinding

class HideTestFragment : BaseBindingFragment<FragmentHideTestBinding>(R.layout.fragment_hide_test) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("hideTest", "隐藏回调:" + hidden)
    }

    override fun onStart() {
        super.onStart()
        Log.d("hideTest", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hideTest", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hideTest", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hideTest", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hideTest", "onDestroy")
    }

}