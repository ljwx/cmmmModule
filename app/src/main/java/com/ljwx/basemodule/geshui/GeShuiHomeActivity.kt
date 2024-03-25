package com.ljwx.basemodule.geshui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.ActivityGeShuiBinding

@Route(path = ConstGeShui.ROUTER_HOME)
class GeShuiHomeActivity : BaseBindingActivity<ActivityGeShuiBinding>(R.layout.activity_ge_shui) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarLight(false)
        commonProcessSteps()
        mBinding.image.setImageResource(R.mipmap.home)

    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.left.singleClick {
            mBinding.image.setImageResource(R.mipmap.home)
        }
        mBinding.right.singleClick {
            mBinding.image.setImageResource(R.mipmap.user)
        }
        mBinding.mingxi.singleClick {
            routerTo(ConstGeShui.ROUTER_YEAR).withFromType(ConstGeShui.FROM_TYPE_YEAR).start()
        }
    }

}