package com.sisensing.common.ble

import android.content.Intent
import android.provider.Settings
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NotificationUtils
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.basenotification.BaseNotificationDndUtils
import com.sisensing.common.router.RouterActivityPath


object NotificationPermissionsUtils {

    fun isEnable(): Boolean {
        return (NotificationUtils.areNotificationsEnabled() && BaseNotificationDndUtils.hasPermissionByPassDnd())
    }

    @JvmStatic
    fun show(view: View) {
        if (!NotificationUtils.areNotificationsEnabled() || !BaseNotificationDndUtils.hasPermissionByPassDnd()) {
            view.visibleGone(true)
        } else {
            view.visibleGone(false)
        }
    }

    @JvmStatic
    fun click(view: View) {
        view.singleClick {
            if (!NotificationUtils.areNotificationsEnabled()) {
                val intent = Intent()
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                // For Android 5 - 7
                intent.putExtra("app_package", AppUtils.getAppPackageName())
                intent.putExtra("app_uid", Utils.getApp().applicationInfo.uid)
                // For Android 8 and above
                intent.putExtra("android.provider.extra.APP_PACKAGE", AppUtils.getAppPackageName())
                ActivityUtils.startActivity(intent)
            } else if (!BaseNotificationDndUtils.hasPermissionByPassDnd()) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                ActivityUtils.startActivity(intent)
//                startActivityForResult(intent, 1)
            }
        }
    }

}