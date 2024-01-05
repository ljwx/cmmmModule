package com.sisensing.common.glucose.alarm

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.sisensing.common.dialog.DialogGlucoseAlarm
import com.sisensing.common.entity.notification.FcmMessageBean

object FriendAlarmUtils {

    private var dialogVeryLow: DialogGlucoseAlarm? = null
    private var dialogLow: DialogGlucoseAlarm? = null
    private var dialogHigh: DialogGlucoseAlarm? = null
    private var dialogLose: DialogGlucoseAlarm? = null

    const val GLUCOSE_LOW = 1
    const val GLUCOSE_HIGH = 2
    const val VERY_LOW = 3
    const val SIGNAL_LOSS = 4

    fun initDialog(context: Context) {

    }

    fun showAlarm(context: Context, fcmMessageBean: FcmMessageBean) {
        createDialog(context, fcmMessageBean.eventType)
        when (fcmMessageBean.eventType) {
            GLUCOSE_LOW -> {
                Log.d("消息通知", "修改低血糖弹窗数据")
                changeData(dialogLow, fcmMessageBean)
            }

            GLUCOSE_HIGH -> {
                changeData(dialogHigh, fcmMessageBean)
            }

            VERY_LOW -> {
                changeData(dialogVeryLow, fcmMessageBean)
            }

            SIGNAL_LOSS -> {
                changeData(dialogLose, fcmMessageBean)
            }
        }
    }

    private fun changeData(dialog: DialogGlucoseAlarm?, fcmMessageBean: FcmMessageBean) {
        dialog?.changeFriendData(fcmMessageBean)
        dialog?.show()
    }

    private fun createDialog(context: Context, type: Int?) {
        if (type == null) {
            return
        }
        when (type) {
            GLUCOSE_LOW -> {
                if (dialogLow != null) {
                    dialogLow?.dismiss()
                    dialogLow = null
                }
                if (dialogLow == null) {
                    Log.d("消息通知", "创建低血糖弹窗")
                    dialogLow = DialogGlucoseAlarm(ActivityUtils.getTopActivity())
                }
            }

            GLUCOSE_HIGH -> {
                if (dialogHigh != null) {
                    dialogHigh?.dismiss()
                    dialogHigh = null
                }
                if (dialogHigh == null) {
                    dialogHigh = DialogGlucoseAlarm(ActivityUtils.getTopActivity())
                }
            }

            VERY_LOW -> {
                if (dialogVeryLow != null) {
                    dialogVeryLow?.dismiss()
                    dialogVeryLow = null
                }
                if (dialogVeryLow == null) {
                    dialogVeryLow = DialogGlucoseAlarm(ActivityUtils.getTopActivity())
                }
            }

            SIGNAL_LOSS -> {
                if (dialogLose != null) {
                    dialogLose?.dismiss()
                    dialogLose = null
                }
                if (dialogLose == null) {
                    dialogLose = DialogGlucoseAlarm(ActivityUtils.getTopActivity())
                }
            }
        }
    }

}