package com.ljwx.basemodule

import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.jeremyliao.liveeventbus.LiveEventBus
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.baseactivity.fast.QuickTabLayoutActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.util.MemoryUtils
import com.ljwx.baseapp.vm.EmptyViewModel
import com.ljwx.baseeventbus.flow.FlowEventBus
import com.ljwx.basemodule.databinding.ActivitySecondBinding

class SecondActivity :
    QuickTabLayoutActivity<ActivitySecondBinding, EmptyViewModel>(R.layout.activity_second) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.button.singleClick {
            FlowEventBus.get<String>("flow").post(this@SecondActivity, "flowevent")
            LiveEventBus.get<String>("liveeventbus").post("liveEvent")
            MemoryUtils.requestMemory()
        }
        mBinding.send.singleClick {
            sendFinishBroadcast("test")
        }

    }

    override fun getTabLayout(): TabLayout = mBinding.tabLayout

    override fun getViewPager2(): ViewPager2 = mBinding.viewPager
}