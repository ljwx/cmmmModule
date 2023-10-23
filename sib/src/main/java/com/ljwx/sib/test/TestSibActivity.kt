package com.ljwx.sib.test

import android.os.Bundle
import com.ljwx.baseapp.vm.empty.EmptyViewModel
import com.ljwx.sib.R
import com.ljwx.sib.activity.BaseSibMVVMActivity
import com.ljwx.sib.databinding.ActivityTestSactivityBinding

class TestSibActivity : BaseSibMVVMActivity<ActivityTestSactivityBinding, EmptyViewModel>(R.layout.activity_test_sactivity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sactivity)
    }
}