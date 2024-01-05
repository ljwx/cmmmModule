package com.sisensing.common.entity.alarm

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.R
import com.sisensing.common.glucose.alarm.FriendAlarmUtils
import com.sisensing.common.entity.notification.FcmMessageBean
import com.sisensing.common.utils.BgUnitUtils
import com.sisensing.common.utils.GlucoseAlarmUtils
import java.text.SimpleDateFormat

class GlucoseAlarmDialogData(val type: Int?, val value: String?) {

    val title: String
        get() {
            if (friendBean != null) {
                return when (type) {
                    FriendAlarmUtils.VERY_LOW -> StringUtils.getString(R.string.urgent_low_glucose)
                    FriendAlarmUtils.GLUCOSE_LOW -> StringUtils.getString(R.string.low_glucose)
                    FriendAlarmUtils.GLUCOSE_HIGH -> StringUtils.getString(R.string.high_glucose)
                    FriendAlarmUtils.SIGNAL_LOSS -> StringUtils.getString(R.string.bsmonitoring_sensor_not_connect_please_check)
                    else -> ""
                }
            } else {
                return when (type) {
                    GlucoseAlarmUtils.VERY_LOW -> StringUtils.getString(R.string.urgent_low_glucose)
                    GlucoseAlarmUtils.GLUCOSE_LOW -> StringUtils.getString(R.string.low_glucose)
                    GlucoseAlarmUtils.GLUCOSE_HIGH -> StringUtils.getString(R.string.high_glucose)
                    GlucoseAlarmUtils.SIGNAL_LOSS -> StringUtils.getString(R.string.bsmonitoring_sensor_not_connect_please_check)
                    else -> ""
                }
            }
        }

    val time: String
        get() {
            return TimeUtils.getNowString(SimpleDateFormat("HH:mm"))
        }

    val content: String
        get() {
            if (friendBean == null) {
                if (type == null) {
                    return ""
                }
                if (type == GlucoseAlarmUtils.SIGNAL_LOSS) {
                    return StringUtils.getString(R.string.unable_to_receive_glucose_readings_from_your_sensor_please_check_the_connection)
                } else {
                    return StringUtils.getString(R.string.current_glucose) + ": " + value + " " + BgUnitUtils.getUserUnit()
                }
            } else {
                val nick = friendBean!!.nickName + " "
                if (type == FriendAlarmUtils.SIGNAL_LOSS) {
                    return StringUtils.getString(
                        R.string.unable_to_receive_glucose_readings_from_friend,
                        nick
                    )
                } else {
                    val unit =
                        if (BgUnitUtils.isTypeMol(friendBean?.unit)) BgUnitUtils.getMolUnit() else BgUnitUtils.getMgUnit()
                    val suffix = "$value $unit"
//                    return nick + StringUtils.getString(R.string.current_glucose) + ": " + value + " " + unit
                    return StringUtils.getString(R.string.friends_current_glucose_value, nick, suffix)
                }
            }
        }

    var friendBean: FcmMessageBean? = null

}