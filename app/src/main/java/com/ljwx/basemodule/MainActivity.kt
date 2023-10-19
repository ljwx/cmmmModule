package com.ljwx.basemodule

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.TAG_CLASS
import com.ljwx.baseapp.extensions.singleClick
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


        super.onCreate(savedInstanceState)
        addTabFragment("staterefresh", StateRefreshFragment())
        addTabFragment("basefragment", BaseFragmentTest())
//        addTabFragment("mvvmFragment", LoginFragment())
//        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("loadmore", LoadMoreFragment())
//        addTabFragment("vmFragment", ViewModelFragment())
//        addTabFragment("javaTest", TestJavaFragment(0))

        registerFinishBroadcast("test")

        mBinding.button.singleClick {
            routerTo("/app/router_test").with("test", TestData(999)).start()
        }

    }

    override fun onBroadcastPageFinish() {
        Log.d("ljwx2", "接收到结束广播")
//        super.onBroadcastPageFinish()
    }

    override fun onBroadcastOther(action: String?) {
        super.onBroadcastOther(action)
        Log.d("ljwx2", "收到广播")
    }

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("ljwx2", "保存数据")
        outState.putString("save", "saveCache")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("ljwx2", "恢复数据:" + savedInstanceState.getString("save", "空的"))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ljwx2", "$TAG_CLASS:onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}