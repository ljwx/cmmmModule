package com.ljwx.basemodule.geshui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.ActivityGeShuiBinding
import com.ljwx.basemodule.databinding.GeshuiFragmentBinding
import com.ljwx.basemodule.databinding.GeshuiFragmentListBinding

@Route(path = ConstGeShui.ROUTER_LIST)
class GeShuiListActivity  : BaseBindingActivity<GeshuiFragmentListBinding>(R.layout.geshui_fragment_list) {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarLight(false)
        show()

    }

    fun show() {

    }


}