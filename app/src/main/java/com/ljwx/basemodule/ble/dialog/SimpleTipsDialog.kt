package com.sisensing.common.ble.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.sisensing.common.R
import com.sisensing.common.ble.LocalBleManager
import com.sisensing.common.constants.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SimpleTipsDialog {

    private var area: DialogCommonTips? = null

    @JvmStatic
    fun showAreaTips(activity: Activity?, valid: Boolean, areaCode: String?) {
        showAuthFail()
        return
            GlobalScope.launch(Dispatchers.IO) {
                delay(500)
                withContext(Dispatchers.Main) {
                    ActivityUtils.getTopActivity()?.let { activity ->
                        disconnect(activity)
                        area?.dismiss()
//                        area = AlertDialog.Builder(activity).setMessage(
//                            "当前设备地区码:$areaCode"
//                        ).setNegativeButton("区域无效,已停止连接") { dialog, which ->
//                            dialog.dismiss()
//                        }.create()
                        area = DialogCommonTips(activity)
                        area?.setCanceledOnTouchOutside(false)
                        area?.setText(
                            activity.getString(R.string.unable_to_connect),
                            activity.getString(R.string.can_not_connect_area_tips),
                            activity.getString(R.string.common_i_know)
                        )
                        area?.show()
                    }
                }
            }
    }

    private var auth: DialogCommonTips? = null

    @JvmStatic
    fun showAuthFail() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(500)
            withContext(Dispatchers.Main) {
                ActivityUtils.getTopActivity()?.let { activity ->
                    disconnect(activity)
//                    auth = auth ?: AlertDialog.Builder(activity).setMessage(
//                        "鉴权失败,当前设备和APP不匹配,请按要求连接设备"
//                    ).setPositiveButton("好的") { dialog, which ->
//                        dialog.dismiss()
//                    }.create()
                    auth = DialogCommonTips(activity)
//                    auth?.setText(
//                        activity.getString(R.string.unable_to_connect),
//                        activity.getString(R.string.can_not_connect_channel_tips),
//                        activity.getString(R.string.common_i_know)
//                    )
                    auth?.setText(
                        StringUtils.getString(R.string.incompatible_sensor),
                        StringUtils.getString(R.string.this_sensor_cannot_be_used_with_this_version_of_the_app),
                        StringUtils.getString(R.string.personalcenter_ok)
                    )
                    auth?.show()
                }
            }
        }
    }

    private fun disconnect(activity: Activity) {
        LocalBleManager.getInstance().stopConnect()
        val intent = Intent(Constant.SENSOR_EXCEPTION_AND_INVALID_BROAD_CAST)
        intent.putExtra(
            Constant.DEVICE_DAMAGE_OR_FAILURE_KEY,
            Constant.SENSOR_INVALID
        )
        intent.putExtra(
            "String",
            "area"
        )
        activity.sendBroadcast(intent)
    }

}