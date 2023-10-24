package com.ljwx.sib.test

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.vm.BaseAndroidViewModel
import com.ljwx.baseapp.vm.empty.EmptyAndroidViewModel
import com.ljwx.baseapp.vm.model.BaseDataRepository
import com.ljwx.sib.R
import com.ljwx.sib.activity.BaseSibMVVMActivity
import com.ljwx.sib.databinding.ActivityTestSactivityBinding

class TestSibActivity<V : ViewDataBinding, VM : BaseAndroidViewModel<M>, M : BaseDataRepository<S>, S> : BaseSibMVVMActivity<V, VM>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test_sactivity
    }
}