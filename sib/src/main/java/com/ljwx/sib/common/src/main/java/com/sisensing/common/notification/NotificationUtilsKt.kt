package com.sisensing.common.notification

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.ljwx.basenotification.BaseNotificationChannel
import com.ljwx.basenotification.BaseNotificationDndUtils
import com.sisensing.common.R
import com.sisensing.common.entity.notification.FcmMessageBean
import com.sisensing.common.utils.Log
import kotlin.random.Random


object NotificationUtilsKt {

    /**
     * 渠道初始化设置
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChannel(
        channelId: String,
        name: String,
        voiceId: Int? = null
    ): NotificationChannel {
        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(false)
        channel.enableLights(false)
        channel.enableVibration(true)
        if (voiceId != null) {
            channel.setSound(getSound(voiceId), Notification.AUDIO_ATTRIBUTES_DEFAULT)
        }
        return channel
    }

    /**
     * 获取自定义铃声资源
     */
    private fun getSound(voiceId: Int): Uri {
        val packageName = AppUtils.getAppPackageName()
        val uri = Uri.parse("android.resource://$packageName/$voiceId")
        return uri
    }

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel(
        channelId: String,
        voiceId: Int? = null,
        name: String? = null
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(channelId) == null) {
                val channel = initChannel(channelId, name ?: channelId, voiceId)
                manager.createNotificationChannel(channel)
            }
        }
    }

    /**
     * 创建所有自定义铃声渠道
     */
    fun createForceChannel() {
        val channelUtils = BaseNotificationChannel()
        ConstNotification.SOUNDS_FORCE_MAP.forEach {
            val channel = BaseNotificationChannel.Builder(it.key)
                .setImportance(NotificationManager.IMPORTANCE_DEFAULT)
                .setBypassDnd(true)
                .setSound(getSound(it.value))
                .enableSound(true)
                .create()
            channelUtils.createNotificationChannel(channel)
        }

    }

    fun createDefaultChannel() {
        val channelUtils = BaseNotificationChannel()
        ConstNotification.SOUND_NORMAL_MAP.forEach {
            val build = BaseNotificationChannel.Builder(it.key)
                .setImportance(NotificationManager.IMPORTANCE_DEFAULT)
                .setBypassDnd(false)
                .enableSound(true)
            it.value?.let {
                build.setSound(getSound(it))
            }
            channelUtils.createNotificationChannel(build.create())
        }
    }

    /**
     * 创建通知
     */
    private fun createNotification(
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): Notification {
        val context = Utils.getApp()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId)
            //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
            var pendingIntent: PendingIntent?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                );
            } else {
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            val build = Notification.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_notification)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setCategory(Notification.CATEGORY_EMAIL)
            build.build()
        } else {
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher_cgm)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent).build()
        }
    }

    /**
     * 发送通知
     */
    fun sendFcmNotification(channelId: String, title: String, content: String) {
        Log.d("推送", "创建通知,channelId:$channelId,title:$title")
        val notificationId = Random.nextInt(1, 10000)
        val intent = NotificationIntentUtils.getFcmIntent(notificationId)
        var validChannelId = channelId
        if (!BaseNotificationDndUtils.hasPermissionByPassDnd()) {
            if (!channelId.contains(ConstNotification.NO_FORCE_TAG)) {
                validChannelId = channelId + ConstNotification.NO_FORCE_TAG
            }
        }
        Log.d("推送", "实际通知,channelId:$validChannelId,title:$title")
        val notification = createNotification(validChannelId, title, content, intent)
        val manager =
            Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    /**
     * 是否有勿扰例外权限
     */
    fun hasPoliceAccess(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val manager =
                Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return manager.isNotificationPolicyAccessGranted
        }
        return true
    }

    /**
     * 跳转勿扰例外设置
     */
    fun requestPoliceAccess(activity: Activity, requestCode: Int) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        activity.startActivityForResult(intent, requestCode)
    }

    @JvmStatic
    fun getFcmData(sijoyData: String?): FcmMessageBean? {
        if (!sijoyData.isNullOrBlank()) {
            return try {
                GsonUtils.fromJson(sijoyData, FcmMessageBean::class.java)
            } catch (e: Exception) {
                Log.d("消息通知", "string解析异常:"+e)
                null
            }
        } else {
            Log.d("消息通知", "fcm的sijoyData为空")
        }
        return null
    }

}