package com.ljwx.basenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils

class BaseNotificationChannel {

    private val TAG = "BaseNotification-" + this.javaClass.simpleName

    fun getNotificationManager(): NotificationManager {
        return Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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

        private var channelName: CharSequence? = null
        private var channelImportance = NotificationManager.IMPORTANCE_DEFAULT
        private var channelEnableVibration: Boolean? = null
        private var channelEnableLights: Boolean? = null
        private var channelEnableSound: Boolean? = null
        private var channelCustomSound: Uri? = null
        private var channelByPassDnd: Boolean? = null

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
            val packageName = AppUtils.getAppPackageName()
            val uri = Uri.parse("android.resource://$packageName/$rawId")
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
            channelEnableSound?.let {
                if (!it) {
                    channel.setSound(null, null)
                }
                if (it && channelCustomSound != null) {
                    channel.setSound(channelCustomSound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
                }
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