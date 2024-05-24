package com.ljwx.baseapp.util

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.constant.BaseLogTag

object LocalEventUtils {

    fun sendAction(action: String?, type: Long?) {
        if (action == null) {
            return
        }
        BaseModuleLog.d(BaseLogTag.LOCAL_EVENT, "发送事件广播:$action")
        Utils.getApp()?.let {
            val intent = Intent(action)
            type?.let {
                intent.putExtra(BaseConstBundleKey.LOCAL_EVENT_COMMON_TYPE, type)
            }
            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
        }
    }

}