package com.ljwx.basemodule

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.showToast
import com.ljwx.baseapp.vm.EmptyViewModel
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*

class MainActivity : QuickMainActivity<ActivityMainBinding, EmptyViewModel>(R.layout.activity_main) {

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        addTabFragment("mvvmFragment", LoginFragment())
        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("basefragment", BaseFragmentTest())
        addTabFragment("loadmore", LoadMoreFragment())
        addTabFragment("vmFragment", ViewModelFragment())
//        addTabFragment("javaTest", TestJavaFragment(0))

    }

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

}