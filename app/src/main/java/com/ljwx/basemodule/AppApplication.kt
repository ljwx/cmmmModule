package com.ljwx.basemodule

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.ljwx.baserefresh.BaseRefreshHeader
import com.ljwx.baserefresh.BaseRefreshLayout

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        BaseRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            BaseRefreshHeader(context, null, 0).getHeader()
        }

        Utils.init(this)
        //初始化阿里路由框架
        if (BuildConfig.DEBUG) {
            ARouter.openLog() //
            ARouter.openDebug() // 开启调试模式
        }
        ARouter.init(this)

    }

}