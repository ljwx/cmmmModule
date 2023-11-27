package com.ljwx.basemodule

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*
import com.ljwx.basemodule.service.TestForegroundService
import com.ljwx.basemodule.vm.TestData
import com.ljwx.basemodule.vm.TestViewModel
import com.ljwx.provideclipboardauto.ClipboardFragment

class MainActivity :
    QuickMainActivity<ActivityMainBinding, TestViewModel>(R.layout.activity_main) {

    private val dialog by lazy {
        TestDialog()
    }

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        addTabFragment("staterefresh", StateRefreshFragment())
        addTabFragment("basefragment", BaseFragmentTest())
//        addTabFragment("mvvmFragment", LoginFragment())
//        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("loadmore", LoadMoreFragment())
//        addTabFragment("vmFragment", ViewModelFragment())
        addTabFragment("javaTest", ClipboardFragment())

        unregisterLocalEvent("test4")

        mBinding.button.singleClick {
            routerTo(ConstRouter.SECOND_ACTIVITY).with("test", TestData(999)).start()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, TestForegroundService::class.java))
        }
    }

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun TestViewModel.scope() {
        mResponse.observe {

        }
    }

}