package com.ljwx.basenotification

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import com.blankj.utilcode.util.Utils

object BaseNotificationUtils {

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    fun getNotificationManager(): NotificationManager {
        return Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun hasByPassDnd(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "通过notificationManager判断是否有勿扰权限")
            return getNotificationManager().isNotificationPolicyAccessGranted
        }
        Log.d(TAG, "android版本低于 M 直接返回ture")
        return true
    }

    fun registerForActivityResult(
        activity: ComponentActivity,
        callback: ActivityResultCallback<Boolean>
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(PolicyAccessSettingsResultContract(), callback)
    }

    fun requestByPassDnd(launcher: ActivityResultLauncher<String>) {
        if (!hasByPassDnd()) {
            launcher.launch("")
            Log.d(TAG, "没有权限发起请求")
        } else {
            Log.d(TAG, "已有权限,无操作")
        }
    }

    fun play() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//类型通知  默认
        val rt = RingtoneManager.getRingtone(Utils.getApp(), uri);
        rt.play();
    }


}