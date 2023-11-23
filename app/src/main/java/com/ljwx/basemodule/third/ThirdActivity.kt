package com.ljwx.basemodule.third

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseMVVMActivity
import com.ljwx.baseapp.vm.AppScopeVM
import com.ljwx.baseapp.vm.ViewModelScope
import com.ljwx.basemodule.R
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivityThirdBinding
import com.ljwx.basemodule.vm.TestViewModel
import com.ljwx.basemodule.vm.UserInfoVM

@Route(path = ConstRouter.THIRD_ACTIVITY)
class ThirdActivity :
    BaseMVVMActivity<ActivityThirdBinding, TestViewModel>(R.layout.activity_third) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppScopeVM.get<UserInfoVM>().userName.value = "third"

    }

    override fun TestViewModel.scope() {

    }
}