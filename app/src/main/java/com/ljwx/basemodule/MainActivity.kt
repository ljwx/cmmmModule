package com.ljwx.basemodule

import android.os.Bundle
import com.ljwx.baseactivity.fast.FastMainActivity
import com.ljwx.baseapp.BaseViewModel
import com.ljwx.basemodule.databinding.ActivityMainBinding

class MainActivity : FastMainActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mTabLayout = mBinding.tabLayout
        mViewPager = mBinding.viewPager
        addTabFragment("loadmore", RecyclerViewFragment())
        attachTabLayoutViewPager()

    }
}

