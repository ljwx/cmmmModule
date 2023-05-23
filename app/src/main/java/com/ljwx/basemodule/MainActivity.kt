package com.ljwx.basemodule

import android.os.Bundle
import com.ljwx.baseactivity.fast.FastMainActivity
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.EmptyViewModel
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*

class MainActivity : FastMainActivity<ActivityMainBinding, EmptyViewModel>(R.layout.activity_main) {

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("basefragment", BaseFragmentTest())
        addTabFragment("loadmore", LoadMoreFragment())
        addTabFragment("javaTest", TestJavaFragment(0))

    }
}