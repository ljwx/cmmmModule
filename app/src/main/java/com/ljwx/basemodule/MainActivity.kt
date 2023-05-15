package com.ljwx.basemodule

import android.os.Bundle
import com.ljwx.baseactivity.fast.FastMainActivity
import com.ljwx.baseapp.BaseViewModel
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.BaseFragmentTest
import com.ljwx.basemodule.fragments.BaseToolbarFragment
import com.ljwx.basemodule.fragments.RecyclerViewFragment
import com.ljwx.basemodule.fragments.TestJavaFragment

class MainActivity : FastMainActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("basefragment", BaseFragmentTest())
        addTabFragment("loadmore", RecyclerViewFragment())
        addTabFragment("javaTest", TestJavaFragment(0))

    }
}