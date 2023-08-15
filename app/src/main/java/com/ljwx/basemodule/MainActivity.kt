package com.ljwx.basemodule

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.jeremyliao.liveeventbus.LiveEventBus
import com.ljwx.baseactivity.fast.QuickMainActivity
import com.ljwx.baseapp.extensions.TAG_CLASS
import com.ljwx.baseapp.extensions.getString
import com.ljwx.baseapp.extensions.showToast
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.baseapp.vm.EmptyViewModel
import com.ljwx.baseeventbus.SimpleFlowEventBus
import com.ljwx.baseeventbus.flow.FlowEventBus
import com.ljwx.basemodule.databinding.ActivityMainBinding
import com.ljwx.basemodule.fragments.*
import com.ljwx.basemodule.vm.TestViewModel
import com.ljwx.baseswitchenv.AppConfigItem
import com.ljwx.baseswitchenv.AppEnvConfig
import com.ljwx.baseswitchenv.ShakeSelectAppEnv
import com.ljwx.baseswitchenv.registerShakeEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity :
    QuickMainActivity<ActivityMainBinding, TestViewModel>(R.layout.activity_main) {

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

        registerFinishBroadcast("test")

        Log.d("ljwx2", TAG_CLASS + "onCreate")
        FlowEventBus.get<String>("flow").observe(this) {
            Log.d("ljwx2", it)
        }
        LiveEventBus.get<String>("liveeventbus").observe(this) {
            Log.d("ljwx2", it)
            mBinding.tabLayout.visibleGone(false)
        }

        AppEnvConfig.addEnv(AppConfigItem("测试", "www.baidu.com"))

        registerShakeEnv(BuildConfig.DEBUG, object :ShakeSelectAppEnv.EnvCallback{
            override fun selected(item: AppConfigItem) {
                Log.d("ljwx2", item.host)
            }
        })

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

}