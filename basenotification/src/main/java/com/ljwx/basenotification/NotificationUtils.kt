package com.ljwx.basenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import kotlin.random.Random

object NotificationUtils {

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
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT)
        if (voiceId != null) {
            channel.setSound(getSound(voiceId), Notification.AUDIO_ATTRIBUTES_DEFAULT)
            Log.d("ljwx2", "设置声音:" + voiceId)
//            modifySound(channel, voiceId)
        } else {
//            channel.setSound(getSound(R.raw.notification_1), Notification.AUDIO_ATTRIBUTES_DEFAULT)
        }
        return channel
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
                Log.d("ljwx2", "创建渠道:$channelId,voiceId:$voiceId")
                val channel = initChannel(channelId, name ?: channelId, voiceId)
                manager.createNotificationChannel(channel)
            }
        }
    }

    private fun deleteNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(channelId) != null) {
                Log.d("ljwx2", "删除渠道:" + channelId)
                manager.deleteNotificationChannel(channelId)
            }
        }
    }

    private fun getSound(voiceId: Int): Uri {
        val packageName = AppUtils.getAppPackageName()
        val uri = Uri.parse("android.resource://$packageName/$voiceId")
        Log.d("ljwx2", "音频文件:$uri")
        return uri
    }

    fun modifySound(channelId: String, voiceId: Int?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deleteNotificationChannel(channelId)
            createNotificationChannel(channelId, voiceId)
//            val manager =
//                Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val channel = manager.getNotificationChannel(channelId)
//            if (channel != null && voiceId != null) {
//                val packageName = AppUtils.getAppPackageName()
//                val uri = Uri.parse("android.resource://$packageName/$voiceId")
//                channel.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
//                manager.createNotificationChannel(channel)
//            }
        }
    }

    fun modifySound(channel: NotificationChannel, voiceId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (channel != null && voiceId != null) {
                val packageName = AppUtils.getAppPackageName()
                val uri = Uri.parse("android.resource://$packageName/$voiceId")
                channel.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
                manager.createNotificationChannel(channel)
            }
        }
    }

    fun createNotification(
        channelId: String,
        title: String,
        content: String,
        intent: Intent
    ): Notification {
        val context = Utils.getApp()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId)
            //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
            var pendingIntent:PendingIntent?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
//            val pendingIntent =
//                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            Notification.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setAutoCancel(true)
//                .setSound(RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSound(uri)
                .setContentIntent(pendingIntent).build()
        } else {
            //PendingIntent.FLAG_UPDATE_CURRENT 这个类型才能传值
            val pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0))
                .setContentIntent(pendingIntent).build()
        }
        //channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + com.sisensing.common.R.raw.alarm_voice2), Notification.AUDIO_ATTRIBUTES_DEFAULT);
    }

    fun sendFcmNotification(channelId: String, title: String, content: String) {
        val notificationId = Random.nextInt(1, 10000)
        val intent = Intent()
        val notification = createNotification(channelId, title, content, intent)
        notification.defaults = Notification.DEFAULT_ALL
        val manager =
            Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
        play()
    }

    fun play() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//类型通知  默认
        val rt = RingtoneManager.getRingtone(Utils.getApp(), uri);
        rt.play();
    }

//    fun sendNotificationTest(context: Context) {
//        val sijoyData = "{\"sijoy.data\":\"{\\\"code\\\":1001,\\\"sound\\\":\\\"sound2.mp3\\\"}\"}";
//        val random = Random().nextInt(10000)
//        //       code   消息类型, 1001:血糖报警；1002:好友血糖报警；1003:邀请
//        var code = 1001
//        val intent = Intent(
//            context,
//            NotificationClickReceiver::class.java
//        )
//        intent.action = NotificationClickReceiver.CLICK_NOTIFICATION
//        intent.putExtra("notificationId", random)
//        NotificationUtils.sendNotification(
//            context,
//            "ljwx",
//            "test",
//            NotificationConfig.DEF_ICONS, intent, random
//        )
//    }


}