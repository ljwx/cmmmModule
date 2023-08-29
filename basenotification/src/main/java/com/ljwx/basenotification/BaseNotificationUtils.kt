package com.ljwx.basenotification

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.NotificationManagerCompat
import com.blankj.utilcode.util.Utils

object BaseNotificationUtils {

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    fun getNotificationManager(): NotificationManager {
        return Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    

    fun play() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//类型通知  默认
        val rt = RingtoneManager.getRingtone(Utils.getApp(), uri);
        rt.play();
    }

    

}