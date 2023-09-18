package com.ljwx.basemodule

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.ljwx.baserefresh.BaseRefreshHeader
import com.ljwx.baserefresh.BaseRefreshLayout
import com.ljwx.baserefresh.SmartRefreshHeader

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        BaseRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            BaseRefreshHeader(context, null, 0).getHeader()
        }

        Utils.init(this)

    }

}