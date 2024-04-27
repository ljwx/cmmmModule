package com.ljwx.baseapp.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ljwx.baseapp.constant.BaseLogTag

object LocalBroadcastUtils {

    fun sendLocalEvent(context: Context, action: String?) {
        BaseModuleLog.d(BaseLogTag.LOCAL_EVENT, action ?: "空")
        if (action == null) {
            return
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(action))
    }

}