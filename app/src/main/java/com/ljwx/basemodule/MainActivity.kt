package com.ljwx.basemodule

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.showToast
import com.ljwx.baseapp.vm.EmptyViewModel
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity :
    QuickMainActivity<ActivityMainBinding, EmptyViewModel>(R.layout.activity_main) {

    private val dialog by lazy {
        TestDialog()
    }

    override fun getTabLayout() = mBinding.tabLayout

    override fun getViewPager2() = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTabFragment("basefragment", BaseFragmentTest())
//        addTabFragment("mvvmFragment", LoginFragment())
        addTabFragment("toolbar", BaseToolbarFragment())
        addTabFragment("loadmore", LoadMoreFragment())
        addTabFragment("vmFragment", ViewModelFragment())
//        addTabFragment("javaTest", TestJavaFragment(0))

        registerOtherBroadcast("test")

        Thread{
            Thread.sleep(7000)
            Log.d("ljwx2", "显示dialog")
            runOnUiThread {
                dialog.show(supportFragmentManager)
            }
        }.start()

        lifecycleScope.launch(Dispatchers.IO) {
            delay(7000)

        }

    }

    override fun onBroadcastOther(action: String?) {
        super.onBroadcastOther(action)
        Log.d("ljwx2", "收到广播")
    }

}