package com.ljwx.basemodule.second

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.jeremyliao.liveeventbus.LiveEventBus
import com.ljwx.baseactivity.fast.QuickTabLayoutActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.util.MemoryUtils
import com.ljwx.baseeventbus.flow.FlowEventBus
import com.ljwx.basemodule.R
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivitySecondBinding
import com.ljwx.basemodule.vm.TestData

@Route(path = ConstRouter.SECOND_ACTIVITY)
class SecondActivity :
    QuickTabLayoutActivity<ActivitySecondBinding, SecondViewModel>(R.layout.activity_second) {

    override fun getTabLayout(): TabLayout = mBinding.tabLayout

    override fun getViewPager2(): ViewPager2 = mBinding.viewPager

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

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

    override fun observeData() {
        super.observeData()
        registerLocalEvent("test1") { action, intent ->

        }
        registerLocalEvent(null) { action, intent ->

        }
        registerLocalEvent("test2") { action, intent ->

        }
        registerLocalEvent("test3") { action, intent ->

        }
        registerLocalEvent("test4") { action, intent ->

        }
    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.memory.singleClick {
            FlowEventBus.get<String>("flow").post(this@SecondActivity, "flowevent")
            LiveEventBus.get<String>("liveeventbus").post("liveEvent")
            MemoryUtils.requestMemory()
        }
        mBinding.button.singleClick {
            sendLocalEvent("test1")
            sendLocalEvent("test2")
            sendLocalEvent("test3")
            sendLocalEvent("test4")
        }
        mBinding.task.singleClick {
            mViewModel.intervalPost()
        }
    }

    override fun SecondViewModel.scope() {
        mIntervelTest.observe {
            Log.d("ljwx2", "轮询结果:" + it)
        }
    }

    override fun getAsyncData() {
        super.getAsyncData()
//        mViewModel.requestTest()
    }

}