package com.ljwx.basenotification

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class PolicyAccessSettingsResultContract : ActivityResultContract<String, Boolean>() {
    override fun createIntent(context: Context, input: String): Intent {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
//        if (resultCode == Activity.RESULT_OK) {
//            if (intent?.hasExtra("userInfo") == true) {
//                if (intent.getSerializableExtra("userInfo") != null) {
//                    return intent.getSerializableExtra("userInfo") as UserInfo
//                }
//            }
//        }
        return BaseNotificationDndUtils.hasPermissionByPassDnd()
    }
}