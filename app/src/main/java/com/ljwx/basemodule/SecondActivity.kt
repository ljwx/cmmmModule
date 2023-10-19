package com.ljwx.basemodule

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.jeremyliao.liveeventbus.LiveEventBus
import com.ljwx.baseactivity.fast.QuickTabLayoutActivity
import com.ljwx.baseapp.extensions.showToast
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.util.MemoryUtils
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.empty.EmptyViewModel
import com.ljwx.baseeventbus.flow.FlowEventBus
import com.ljwx.basemodule.databinding.ActivitySecondBinding
import com.ljwx.basemodule.vm.TestData
import com.ljwx.basemodule.vm.TestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = "/app/router_test")
class SecondActivity :
    QuickTabLayoutActivity<ActivitySecondBinding, TestViewModel>(R.layout.activity_second) {

    override fun getTabLayout(): TabLayout = mBinding.tabLayout

    override fun getViewPager2(): ViewPager2 = mBinding.viewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commonProcessSteps()

    }

    override fun getFirstInitData() {
        super.getFirstInitData()
        intent.getParcelableExtra<TestData>("test")?.let {
//            showToast(it.code.toString())
        }
    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.button.singleClick {
            FlowEventBus.get<String>("flow").post(this@SecondActivity, "flowevent")
            LiveEventBus.get<String>("liveeventbus").post("liveEvent")
            MemoryUtils.requestMemory()
        }
        mBinding.send.singleClick {
            sendFinishBroadcast("test")
        }
    }

    override fun TestViewModel.observeData() {
        mResponse.observe {
            showToast("结果回来了")
        }
    }

    override fun getAsyncData() {
        super.getAsyncData()
        mViewModel.requestTest()
    }

}