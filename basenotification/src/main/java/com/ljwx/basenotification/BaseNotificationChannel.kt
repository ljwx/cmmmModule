package com.ljwx.basenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.AppUtils

class BaseNotificationChannel {

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    private fun getNotificationManager(): NotificationManager {
        return BaseNotificationUtils.getNotificationManager()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isExist(channelId: String): Boolean {
        return getNotificationManager().getNotificationChannel(channelId) != null
    }

    fun createNotificationChannel(channel: NotificationChannel, existDelete: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getNotificationManager()
            val channelId = channel.id
            if (existDelete && isExist(channelId)) {
                manager.deleteNotificationChannel(channelId)
                Log.d(TAG, "删除通知渠道成功:${channelId}")
            }
            if (!isExist(channelId)) {
                manager.createNotificationChannel(channel)
                Log.d(TAG, "创建通知渠道成功:${channelId}")
            } else {
                Log.d(TAG, "创建通知渠道失败:渠道已存在")
            }
        } else {
            Log.d(TAG, "创建通知渠道失败:系统版本小于android O(sdk26)")
        }
    }

    class Builder(private val channelId: String) {

        @IntDef(
            NotificationManager.IMPORTANCE_UNSPECIFIED,
            NotificationManager.IMPORTANCE_NONE,
            NotificationManager.IMPORTANCE_MIN,
            NotificationManager.IMPORTANCE_LOW,
            NotificationManager.IMPORTANCE_DEFAULT,
            NotificationManager.IMPORTANCE_HIGH,
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class NotifyImportance

        //渠道名
        private var channelName: CharSequence = channelId

        //重要程度
        private var channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        //是否震动
        private var channelEnableVibration: Boolean? = null

        //是否亮屏
        private var channelEnableLights: Boolean? = null

        //是否提示音
        private var channelEnableSound: Boolean? = null

        //自定义提示音
        private var channelCustomSound: Uri? = null

        //绕过勿扰
        private var channelByPassDnd: Boolean? = null

        //提示音属性
        private var channelAudioAttributes: AudioAttributes? = null

        fun channelName(name: CharSequence): Builder {
            channelName = name
            return this
        }

        fun setImportance(@NotifyImportance importance: Int): Builder {
            channelImportance = importance
            return this
        }

        fun enableVibration(enable: Boolean): Builder {
            channelEnableVibration = enable
            return this
        }

        fun enableLights(enable: Boolean): Builder {
            channelEnableLights = enable
            return this
        }

        fun enableSound(enable: Boolean): Builder {
            channelEnableSound = enable
            return this
        }

        fun setSound(uri: Uri): Builder {
            channelCustomSound = uri
            return this
        }

        fun setSound(@RawRes rawId: Int): Builder {
            val uri = Uri.parse("android.resource://${AppUtils.getAppPackageName()}/$rawId")
            channelCustomSound = uri
            return this
        }

        fun setSound(context: Context, @RawRes rawId: Int): Builder {
            val uri = Uri.parse("android.resource://${context.packageName}/$rawId")
            channelCustomSound = uri
            return this
        }

        fun setBypassDnd(byPassDnd: Boolean): Builder {
            channelByPassDnd = byPassDnd
            return this
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun create(): NotificationChannel {
            channelName = channelName ?: channelId
            val channel = NotificationChannel(channelId, channelName, channelImportance!!)
            channelEnableVibration?.let {
                channel.enableVibration(it)
            }
            channelEnableLights?.let {
                channel.enableLights(it)
            }
            if (channelEnableSound != false && channelCustomSound != null) {
//                val audioAttributes: AudioAttributes = AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build()
                channel.setSound(channelCustomSound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            }
            if (channelEnableSound == false) {
                channel.setSound(null, null)
            }
            channelByPassDnd?.let {
                channel.setBypassDnd(it)
            }
            return channel
        }

    }

}