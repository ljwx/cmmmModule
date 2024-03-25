package com.ljwx.basemodule.geshui

import android.os.Bundle
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.ActivityGeShuiBinding

class GeShuiActivity : BaseBindingActivity<ActivityGeShuiBinding>(R.layout.activity_ge_shui) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarLight(false)
        commonProcessSteps()
        replace(1)
    }

    override fun setClickListener() {
        super.setClickListener()
        var type = 1
        mBinding.right.singleClick {
            type += 1
            replace(type)
        }
    }

    fun replace(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        when (position) {
            1 -> routerTo("/geshui/step").withFromType(1).start()
            2 -> routerTo("/geshui/step").withFromType(2).start()
            3 -> routerTo("/geshui/step").withFromType(3).start()
            4 -> routerTo("/geshui/step").withFromType(4).start()
            5 -> transaction.replace(R.id.container, ListFragment())
        }
        transaction.commit()
    }

}