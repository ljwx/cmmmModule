package com.ljwx.basenotification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.blankj.utilcode.util.Utils
import kotlin.random.Random

object BaseNotificationUtils {

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    fun getNotificationManager(context: Context? = null): NotificationManager {
        val c = context ?: Utils.getApp()
        return c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun send(context: Context, channelId: String, title: String, content: String, intent: Intent) {
        val notificationId = Random.nextInt(1, 10000)
        val notification =
            BaseNotification.createNotification(context, channelId, title, content, intent)
        getNotificationManager(context).notify(notificationId, notification)
        Log.d(TAG, "发送通知,id:${channelId},标题:" + title + ",内容:" + content)
    }

    fun send(notification: Notification, notificationId: Int? = null) {
        val id = notificationId ?: getNotificationId()
        getNotificationManager().notify(id, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "发送创建好的通知,id:" + notification.channelId)
        }
    }

    fun getNotificationId(max: Int = 10000): Int {
        return Random.nextInt(1, max)
    }

    fun playNormalRingtone() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//类型通知  默认
        val rt = RingtoneManager.getRingtone(Utils.getApp(), uri);
        rt.play()
    }


}