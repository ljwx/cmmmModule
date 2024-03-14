package com.ljwx.basemodule

import android.os.Bundle
import com.blankj.utilcode.util.SPUtils
import com.ljwx.baseactivity.BaseBindingActivity
import com.ljwx.basemodule.constance.ConstRouter
import com.ljwx.basemodule.constance.ConstSPKey
import com.ljwx.basemodule.databinding.ActivitySplashBinding

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        routerTo(getFunctionRouter()).start()

    }

    private fun getFunctionRouter(): String {
        val router = SPUtils.getInstance().getString(ConstSPKey.MAIN_FUNCTION_ROUTER)
        return if (router.isNullOrEmpty()) ConstRouter.FUNCTION_DEBUG_MAIN else router
    }

}