package com.ljwx.basemodule

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseMVVMActivity
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivityThirdBinding
import com.ljwx.basemodule.vm.TestViewModel

@Route(path = ConstRouter.THIRD_ACTIVITY)
class ThirdActivity :
    BaseMVVMActivity<ActivityThirdBinding, TestViewModel>(R.layout.activity_third) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun TestViewModel.scope() {

    }
}