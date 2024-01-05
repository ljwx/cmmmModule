package com.sisensing.common

import android.util.Log
import com.sisensing.common.user.GlucoseAlarmGlobal.getThreshold
import com.sisensing.common.utils.GlucoseAlarmUtils
import com.sisensing.common.utils.GlucoseUtils.getFloatValue

class VerifyCode {

    fun alarm() {
        val bsValue = 7.9f
        val compareBsValue = getFloatValue(bsValue, true, false).toInt().toFloat()
        val alarmVery = getThreshold(GlucoseAlarmUtils.VERY_LOW) as Int
        val alarmHigh = getThreshold(GlucoseAlarmUtils.GLUCOSE_HIGH)
        val alarmLow = getThreshold(GlucoseAlarmUtils.GLUCOSE_LOW)
        Log.d(
            "血糖报警",
            "very:$alarmVery-high:$alarmHigh-low:$alarmLow-compareBsValue:$compareBsValue"
        )
        if (alarmHigh != null && compareBsValue > alarmHigh) {
            Log.d("血糖报警", "高血糖了")
        }
        val bsValue2 = 3.0f
        val compareBsValue2 = getFloatValue(bsValue2, true, false).toInt().toFloat()
        if (compareBsValue2 < alarmVery) {
            Log.d("血糖报警", "极低")
        }
        val bsValue3 = 4.7f
        val compareBsValue3 = getFloatValue(bsValue3, true, false).toInt().toFloat()
        if (alarmLow != null && compareBsValue3 < alarmLow) {
            Log.d("血糖报警", "低")
        }
    }

}