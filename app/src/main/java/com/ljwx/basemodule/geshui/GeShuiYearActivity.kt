package com.ljwx.basemodule.geshui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.GeshuiFragmentBinding

@Route(path = ConstGeShui.ROUTER_YEAR)
class GeShuiYearActivity : BaseBindingActivity<GeshuiFragmentBinding>(R.layout.geshui_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commonProcessSteps()
        setStatusBarLight(false)
        mBinding.image.setImageResource(R.mipmap.year)

    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.yearButton.singleClick {
            routerTo(ConstGeShui.ROUTER_LIST).start()
        }
    }


}