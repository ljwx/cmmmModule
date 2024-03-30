package com.ljwx.basemodule.third

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.ljwx.baseactivity.BaseMVVMActivity
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.infochange.BaseUserInfo
import com.ljwx.baseapp.infochange.IBaseUserInfo
import com.ljwx.baseapp.vm.GlobalDataRepository
import com.ljwx.basemodule.R
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.databinding.ActivityThirdBinding
import com.ljwx.basemodule.vm.TestViewModel

@Route(path = ConstRouter.THIRD_ACTIVITY)
class ThirdActivity :
    BaseMVVMActivity<ActivityThirdBinding, TestViewModel>(R.layout.activity_third) {

    override var enableUserInfoChangeListener = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commonProcessSteps()

    }


    override fun setClickListener() {
        super.setClickListener()
        mBinding.button.singleClick {
            val info = object : BaseUserInfo() {

                override fun isLoggedIn(): Boolean {
                    return true
                }

            }
            info.setInfoChangeType(3)
            GlobalDataRepository.postUserInfo(null)
        }
    }

    override fun TestViewModel.scope() {

    }

    override fun userInfoChange(data: IBaseUserInfo?, type: Int) {
        super.userInfoChange(data, type)
        Log.d(TAG, "asdf3")
        if (type == 3) {
        }
    }
}