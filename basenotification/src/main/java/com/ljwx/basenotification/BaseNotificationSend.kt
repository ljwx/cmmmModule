package com.ljwx.basenotification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.Utils
import kotlin.random.Random

object BaseNotificationSend {

    var smallIcon = android.R.drawable.ic_dialog_map

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    private fun getNotificationManager(): NotificationManager {
        return Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * pendingIntent
     */
    private fun createPendingIntentLess31(context: Context, intent: Intent): PendingIntent {
        //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createPendingIntentMore31(context: Context, intent: Intent): PendingIntent {
        //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
        return PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createPendingIntent(context: Context, intent: Intent): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            createPendingIntentMore31(context, intent)
        } else {
            createPendingIntentLess31(context, intent)
        }
    }

    /**
     * notification
     */
    private fun createNotificationBuilderLess26(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): NotificationCompat.Builder {
        //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0))
            .setContentIntent(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationBuilderMore26(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): Notification.Builder {
        val pendingIntent = createPendingIntent(context, intent)
//            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return Notification.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(smallIcon)
//                .setAutoCancel(true)
//                .setSound(RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSound(uri)
            .setContentIntent(pendingIntent)
    }

    private fun createNotification(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "创建android O以上通知,id:${channelId},标题:" + title + ",内容:" + content)
            createNotificationBuilderMore26(context, channelId, title, content, intent).build()
        } else {
            Log.d(TAG, "发送android O以下通知,id:${channelId},标题:" + title + ",内容:" + content)
            createNotificationBuilderLess26(context, channelId, title, content, intent).build()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotifyBuilderMore26(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): Notification.Builder {
        return createNotificationBuilderMore26(context, channelId, title, content, intent)
    }

    fun getNotifyBuilderLess26(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): NotificationCompat.Builder {
        return createNotificationBuilderLess26(context, channelId, title, content, intent)
    }

    fun send(context: Context, channelId: String, title: String, content: String, intent: Intent) {
        val notificationId = Random.nextInt(1, 10000)
        val notification = createNotification(context, channelId, title, content, intent)
        getNotificationManager().notify(notificationId, notification)
        Log.d(TAG, "发送通知,id:${channelId},标题:" + title + ",内容:" + content)
    }

    fun send(notification: Notification) {
        val notificationId = Random.nextInt(1, 10000)
        getNotificationManager().notify(notificationId, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "发送创建好的通知,id:" + notification.channelId)
        }
    }

}