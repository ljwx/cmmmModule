package com.sisensing.common.glucose.alarm

import android.content.Context
import android.util.Log
import com.sisensing.common.dialog.DialogGlucoseAlarm
import com.sisensing.common.entity.notification.FcmMessageBean
import com.sisensing.common.utils.GlucoseAlarmUtils

object GlucoseAlarmDialogUtils {

    private var dialogVeryLow: DialogGlucoseAlarm? = null
    private var dialogLow: DialogGlucoseAlarm? = null
    private var dialogHigh: DialogGlucoseAlarm? = null
    private var dialogLose: DialogGlucoseAlarm? = null

    const val GLUCOSE_LOW = GlucoseAlarmUtils.GLUCOSE_LOW
    const val GLUCOSE_HIGH = GlucoseAlarmUtils.GLUCOSE_HIGH
    const val VERY_LOW = GlucoseAlarmUtils.VERY_LOW
    const val SIGNAL_LOSS = GlucoseAlarmUtils.SIGNAL_LOSS

    @JvmStatic
    fun showAlarm(context: Context, type: Int?, value: String?) {
        createDialog(context, type)
        when (type) {
            GLUCOSE_LOW -> {
                Log.d("消息通知", "修改低血糖弹窗数据")
                changeData(dialogLow, type, value)
            }

            GLUCOSE_HIGH -> {
                changeData(dialogHigh, type, value)
            }

            VERY_LOW -> {
                changeData(dialogVeryLow, type, value)
            }

            SIGNAL_LOSS -> {
                changeData(dialogLose, type, value)
            }
        }
    }

    fun notifyTypeConvert(notifyType: Int?): Int {
        when (notifyType) {
            FcmMessageBean.ALARM_LOW -> return GLUCOSE_LOW
            FcmMessageBean.ALARM_HIGH -> return GLUCOSE_HIGH
            FcmMessageBean.ALARM_VERY -> return VERY_LOW
            FcmMessageBean.ALARM_LOST -> return SIGNAL_LOSS
        }
        return -1
    }

    private fun changeData(dialog: DialogGlucoseAlarm?, type: Int?, value: String?) {
        dialog?.changeData(type, value)
        dialog?.show()
    }

    private fun createDialog(context: Context, type: Int?) {
        if (type == null) {
            return
        }
        when (type) {
            GLUCOSE_LOW -> {
                if (dialogLow == null) {
                    Log.d("消息通知", "创建低血糖弹窗")
                    dialogLow = DialogGlucoseAlarm(context)
                }
            }

            GLUCOSE_HIGH -> {
                if (dialogHigh == null) {
                    dialogHigh = DialogGlucoseAlarm(context)
                }
            }

            VERY_LOW -> {
                if (dialogVeryLow == null) {
                    dialogVeryLow = DialogGlucoseAlarm(context)
                }
            }

            SIGNAL_LOSS -> {
                if (dialogLose == null) {
                    dialogLose = DialogGlucoseAlarm(context)
                }
            }
        }
    }


}