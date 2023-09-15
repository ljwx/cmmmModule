package com.ljwx.basemodule

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.ljwx.baserefresh.BaseRefreshLayout
import com.ljwx.baserefresh.SmartRefreshHeader

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        BaseRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            SmartRefreshHeader(context, null, 0)
        }

        Utils.init(this)

    }

}