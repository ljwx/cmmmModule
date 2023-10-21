package com.ljwx.basemodule

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*
import com.ljwx.basemodule.vm.TestData
import com.ljwx.basemodule.vm.TestViewModel

class MainActivity :
    QuickMainActivity<ActivityMainBinding, TestViewModel>(R.layout.activity_main) {

    private val dialog by lazy {
        TestDialog()
    }

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("ljwx2", Utils.getApp().toString())
        Log.d("ljwx2", applicationContext.toString())

        super.onCreate(savedInstanceState)
        addTabFragment("staterefresh", StateRefreshFragment())
        addTabFragment("basefragment", BaseFragmentTest())
//        addTabFragment("mvvmFragment", LoginFragment())
//        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("loadmore", LoadMoreFragment())
//        addTabFragment("vmFragment", ViewModelFragment())
//        addTabFragment("javaTest", TestJavaFragment(0))

        unregisterLocalEvent("test4")

        mBinding.button.singleClick {
            routerTo(ConstRouter.SECOND_ACTIVITY).with("test", TestData(999)).start()
        }

    }

    override fun onReceiveLocalEvent(action: String) {
        super.onReceiveLocalEvent(action)
        Log.d("ljwx2", "接收到广播:$action")
    }

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun TestViewModel.scope() {
        mResponse.observe {

        }
    }

}