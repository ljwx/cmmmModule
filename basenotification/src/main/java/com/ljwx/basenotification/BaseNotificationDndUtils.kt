object BaseNotificationDndUtils {

    fun isDndEnable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = BaseNotificationUtils.getNotificationManager()
            Log.d(TAG, "通过NotificationManager判断是否勿扰")
            notificationManager.currentInterruptionFilter ==
                    NotificationManager.INTERRUPTION_FILTER_PRIORITY
        } else {
            // 对于 Android 6.0 之前的版本，您可以检查是否启用了“静音模式”
            Log.d(TAG, "6.0之前只能判断是否静音")
            !NotificationManagerCompat.from(Utils.getApp()).areNotificationsEnabled()
        }
    }

    fun hasByPassDnd(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "通过notificationManager判断是否有勿扰权限")
            return BaseNotificationUtils.getNotificationManager().isNotificationPolicyAccessGranted
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
}