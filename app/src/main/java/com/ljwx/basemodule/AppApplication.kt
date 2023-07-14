package com.ljwx.basemodule

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.ljwx.baserefresh.SmartRefreshHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            SmartRefreshHeader(context, null, 0)
        }

        Utils.init(this)

    }

}