package com.ljwx.baseapp.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object LocalBroadcastUtils {

    fun sendLocalEvent(context: Context, action: String?) {
        if (action == null) {
            return
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(action))
    }

}