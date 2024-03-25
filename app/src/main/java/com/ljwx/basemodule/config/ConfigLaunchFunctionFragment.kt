package com.ljwx.basemodule.config

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.constance.ConstSPKey
import com.ljwx.basemodule.databinding.FragmentLaunchFunctionConfigBinding

class ConfigLaunchFunctionFragment :
    BaseBindingFragment<FragmentLaunchFunctionConfigBinding>(R.layout.fragment_launch_function_config) {

    private var functionRouter = ConstRouter.FUNCTION_DEBUG_MAIN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.launchFunctionRg.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.debug -> {
                    functionRouter = ConstRouter.FUNCTION_DEBUG_MAIN
                }

                R.id.geshui -> {
                    functionRouter = ConstRouter.FUNCTION_GESHUI_SPLASH
                }
            }
        }

        mBinding.config.singleClick {
            saveFunctionRouter()
        }

    }

    private fun saveFunctionRouter() {
        SPUtils.getInstance().put(ConstSPKey.MAIN_FUNCTION_ROUTER, functionRouter)
        ToastUtils.showShort("保存成功")
    }

}