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
import com.ljwx.baseapp.infochange.BaseUserInfo
import com.ljwx.baseapp.infochange.IBaseUserInfo
import com.ljwx.baseapp.util.MemoryUtils
import com.ljwx.baseapp.vm.GlobalDataRepository
import com.ljwx.baseeventbus.flow.FlowEventBus
import com.ljwx.basemodule.R
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivitySecondBinding
import com.ljwx.basemodule.fragments.HideTestFragment
import com.ljwx.basemodule.fragments.LoadMoreFragment

@Route(path = ConstRouter.SECOND_ACTIVITY)
class SecondActivity :
    QuickTabLayoutActivity<ActivitySecondBinding, SecondViewModel>(R.layout.activity_second) {

    override fun getTabLayout(): TabLayout = mBinding.tabLayout

    override fun getViewPager2(): ViewPager2 = mBinding.viewPager

    override fun getScreenOrientation() = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    private var hideTestFragment: HideTestFragment? = null
    private var otherFragment: LoadMoreFragment? = null

    override var enableUserInfoChangeListener = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commonProcessSteps()
        addShow()

    }

    override fun getFirstInitData() {
        super.getFirstInitData()
//        intent.getParcelableExtra<TestData>("test")?.let {
////            showToast(it.code.toString())
//        }
    }

    override fun observeData() {
        super.observeData()

    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.memory.singleClick {
            FlowEventBus.get<String>("flow").post(this@SecondActivity, "flowevent")
            LiveEventBus.get<String>("liveeventbus").post("liveEvent")
            MemoryUtils.requestMemory()
        }
        mBinding.button.singleClick {
            routerTo(ConstRouter.THIRD_ACTIVITY).start()
        }
        mBinding.task.singleClick {
//            mViewModel.intervalPost()
            mViewModel.saveStateTest.value = "保存一个值"
        }
    }

    override fun SecondViewModel.scope() {
//        mIntervelTest.observe {
//            Log.d("ljwx2", "轮询结果:" + it)
//        }
    }

    override fun getAsyncData(refresh: Boolean) {
        super.getAsyncData(refresh)
//        mViewModel.requestTest()
    }

    private fun addShow() {
        hideTestFragment = hideTestFragment ?: HideTestFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_test, HideTestFragment()).commit()
    }

    override fun userInfoChange(data: IBaseUserInfo?, type: Int) {
        super.userInfoChange(data, type)
        if (type == 3) {
            Log.d(TAG, "asdf2")
        }
    }

}