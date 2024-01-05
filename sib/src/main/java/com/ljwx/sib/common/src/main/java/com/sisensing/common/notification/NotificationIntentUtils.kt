package com.sisensing.common.notification

import android.content.Intent
import com.blankj.utilcode.util.Utils

object NotificationIntentUtils {

    fun getFcmIntent(notificationId: Int) :Intent{
        val intent = Intent(Utils.getApp(), NotificationClickReceiver::class.java)
        intent.action = NotificationClickReceiver.CLICK_NOTIFICATION
        intent.putExtra("notificationId", notificationId)
        return intent
    }

}