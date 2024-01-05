package com.sisensing.common.entity.alarm

import android.os.Parcelable
import androidx.core.text.isDigitsOnly
import androidx.databinding.ObservableBoolean
import com.blankj.utilcode.util.StringUtils
import com.ljwx.baseapp.extensions.notNullOrBlank
import com.sisensing.common.R
import com.sisensing.common.utils.BgUnitUtils
import com.sisensing.common.utils.GlucoseUtils
import kotlinx.android.parcel.Parcelize
import kotlin.math.max

@Parcelize
data class UserGlucoseAlarmSettingsData(
    var forceRemind: Boolean?, //是否强制提醒
    var highAlarm: GlucoseAlarmItemData?,//高血糖提醒
    var lowAlarm: GlucoseAlarmItemData?,//低血糖提醒
    var signalLostAlarm: GlucoseAlarmItemData?,//信号丢失提醒
    var veryLowAlarm: GlucoseAlarmItemData?,//极低血糖提醒
    var id: String? = null
) : Parcelable {
    var forceRemindEnable = ObservableBoolean(false)
    var highAlarmEnable = ObservableBoolean(false)
    var lowAlarmEnable = ObservableBoolean(false)
    var signalLostEnable = ObservableBoolean(false)
    var veryLowEnable = ObservableBoolean(false)

    init {
        forceRemindEnable.set(forceRemind == true)
        highAlarmEnable.set(highAlarm?.enable == true)
        lowAlarmEnable.set(lowAlarm?.enable == true)
        signalLostEnable.set(signalLostAlarm?.enable == true)
        veryLowEnable.set(veryLowAlarm?.enable == true)
    }

    //血糖极低显示
    val veryLowDisplay: String
        get() = veryLowAlarm?.thresholdDisplay ?: StringUtils.getString(R.string.off)

    //低血糖显示
    val lowDisplay: String
        get() = lowAlarm?.thresholdDisplay ?: StringUtils.getString(R.string.off)

    //高血糖显示
    val highDisplay: String
        get() = highAlarm?.thresholdDisplay ?: StringUtils.getString(R.string.off)

    //信号丢失显示
    val signalLostDisplay: String
        get() = signalLostAlarm?.durationDisplay ?: StringUtils.getString(R.string.off)

    override fun toString(): String {
        return "WholeData(forceRemind=$forceRemind, " +
                "\nhighAlarm      =$highAlarm, " +
                "\nlowAlarm       =$lowAlarm, " +
                "\nsignalLostAlarm=$signalLostAlarm, " +
                "\nveryLowAlarm   =$veryLowAlarm)"
    }


}

@Parcelize
data class GlucoseAlarmItemData(
    var alarmInterval: Int?,//报警间隔
    var enable: Boolean?,//是否开启告警
    var sound: String?,//报警铃声
    var style: Int?,//报警方式,Sound:1，Shaking:2，Shaking&Sound:3
    var threshold: Float?,//阈值
    var duration: Int?//丢失时长（分钟）
) : Parcelable {

//    var duration: Int? = null //丢失时长（分钟）

    //是否开启
    val switchOpen: Boolean
        get() = enable == true

    //阈值显示
    val thresholdDisplay: String
        get() {
            if (enable == true && threshold != null) {
                return GlucoseUtils.getConvertValue(
                    threshold,
                    false,
                    BgUnitUtils.isUserMol()
                ) + BgUnitUtils.getUserUnit()
            } else {
                return StringUtils.getString(R.string.off)
            }
        }

    //间隔提醒显示
    val alarmIntervalDisplay: String
        get() {
            if (duration != null && duration!! > 0) {
                return duration.toString() + StringUtils.getString(R.string.min)
            } else {
                var validInterval = alarmInterval ?: 10
                if (validInterval > 60) {
                    val hour = validInterval / 60
                    val min = validInterval % 60
                    return hour.toString() + StringUtils.getString(R.string.dailybs_hour_h) + " " + min + StringUtils.getString(
                        R.string.min
                    )
                } else {
                    return validInterval.toString() + StringUtils.getString(R.string.min)
                }
            }
        }

    //提醒方式显示
    val styleDisplay: String
        get() {
            if (style == null || style == 2) {
                style = 3
            }
            return when (style) {
                1 -> StringUtils.getString(R.string.sound)
                else -> {
                    StringUtils.getString(R.string.personalcenter_voice_vibrator)
                }
            }
        }

    //铃声显示
    val soundDisplay: String
        get() {
            val list = StringUtils.getStringArray(R.array.alarm_voice_list)
            if (soundIndex < list.size) {
                return list[soundIndex]
            }
            return ""
        }

    val soundIndex: Int
        get() {
            if (sound.notNullOrBlank() && sound?.isDigitsOnly() == true) {
                return sound!!.toInt() - 1
            }
            return 0
        }

    //信号丢失间隔
    val durationDisplay: String
        get() {
            return if (enable == true) StringUtils.getString(R.string.on) else StringUtils.getString(
                R.string.off
            )
        }

    override fun toString(): String {
        return "[alarmInterval=$alarmInterval, " +
                "enable=$enable, " +
                "sound=$sound, " +
                "style=$style, " +
                "threshold=$threshold, " +
                "duration=$duration]"
    }


}
