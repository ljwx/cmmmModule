package com.ljwx.basemodule

import android.os.Bundle
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.databinding.ActivitySecondBinding

class SecondActivity : BaseBindingActivity<ActivitySecondBinding>(R.layout.activity_second) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.button.singleClick {
            sendOtherBroadcast("test")
        }

    }
}