package com.ljwx.baseapp.debug

import android.content.pm.ApplicationInfo
import com.blankj.utilcode.util.Utils

object DebugUtils {

    fun isDebug() :Boolean{
        return 0 != (Utils.getApp().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE)
    }

}