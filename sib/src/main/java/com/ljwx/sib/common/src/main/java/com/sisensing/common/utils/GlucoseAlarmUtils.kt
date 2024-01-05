package com.sisensing.common.utils

import com.sisensing.common.entity.alarm.GlucoseAlarmItemData
import com.sisensing.common.entity.alarm.UserGlucoseAlarmSettingsData
import com.sisensing.common.entity.personalcenter.SharerEntity
import com.sisensing.common.user.UserInfoUtils

object GlucoseAlarmUtils {

    const val GLUCOSE_LOW = 1
    const val GLUCOSE_HIGH = 2
    const val VERY_LOW = 3
    const val SIGNAL_LOSS = 4

    const val VERY_LOW_MOL = 3.1F
    const val VERY_LOW_MG = 55
    const val VERY_LOW_INTERVAL = 30

    const val INTERVAL_DEFAULT = 10
    const val SIGNAL_LOSE_DEFAULT = 30
    const val STYLE_DEFAULT = 1
    const val SOUND_DEFAULT = 1

    private val LOW_MOL = floatArrayOf(3.3f, 5.6f, 0.1f)
    private val LOW_MG = intArrayOf(60, 100, 5)
    private val HIGH_MOL = floatArrayOf(6.7f, 22.2f, 0.1f)
    private val HIGH_MG = intArrayOf(120, 400, 5)
    private val SIGNAL_LOSE = intArrayOf(10, 120, 10)
    private val GLUCOSE_INTERVAL = intArrayOf(0, 55, 5)

    private val LOW_DEFAULT_MG = 70
    private val HIGH_DEFAULT_MG = 250

    @JvmStatic
    val debugType = -1

    @JvmStatic
    fun isDebug(): Boolean {
        return debugType > 0
    }

    /**
     * 低血糖
     */
    fun getLowList(isMol: Boolean): List<String> {
        val list = ArrayList<String>()
        val start: Int = if (isMol) (LOW_MOL[0] * 10).toInt() else LOW_MG[0]
        val end: Int = if (isMol) (LOW_MOL[1] * 10).toInt() else LOW_MG[1]
        val step = if (isMol) (LOW_MOL[2] * 10).toInt() else LOW_MG[2]
        for (i in start..end step step) {
            if (isMol) {
                list.add(GlucoseUtils.getConvertValue(i / 10f, true, true))
            } else {
                list.add(i.toString())
            }
//            list.add((if (isMol) (i / 10f) else i).toString())
        }
        return list
    }

    /**
     * 高血糖
     */
    fun getHighList(isMol: Boolean): List<String> {
        val list = ArrayList<String>()
        val start: Int = if (isMol) (HIGH_MOL[0] * 10).toInt() else HIGH_MG[0]
        val end: Int = if (isMol) (HIGH_MOL[1] * 10).toInt() else HIGH_MG[1]
        val step = if (isMol) (HIGH_MOL[2] * 10).toInt() else HIGH_MG[2]
        for (i in start..end step step) {
            if (isMol) {
                list.add(GlucoseUtils.getConvertValue(i / 10f, true, true))
            } else {
                list.add(i.toString())
            }
//            list.add((if (isMol) (i / 10f) else i).toString())
        }
        return list
    }

    fun getGlucoseInterval(): List<String> {
        val list = ArrayList<String>()
        for (i in GLUCOSE_INTERVAL[0]..GLUCOSE_INTERVAL[1] step GLUCOSE_INTERVAL[2]) {
            list.add(i.toString())
        }
        return list
    }

    /**
     * 信号丢失
     */
    fun getSignalLoseList(): List<String> {
        val list = ArrayList<String>()
        for (i in SIGNAL_LOSE[0]..SIGNAL_LOSE[1] step SIGNAL_LOSE[2]) {
            list.add(i.toString())
        }
        return list
    }

    fun changeDefault(data: UserGlucoseAlarmSettingsData) {
        data?.apply {
            veryLowAlarm?.apply {
                threshold = if (threshold == null || threshold!! < 1) VERY_LOW_MG.toFloat() else threshold
                alarmInterval = if (alarmInterval == null || alarmInterval!! < 1) VERY_LOW_INTERVAL else alarmInterval
                style = if (style == null || style!! < 1) STYLE_DEFAULT else style
                sound = if (sound.isNullOrEmpty()) SOUND_DEFAULT.toString() else sound
            }
            lowAlarm?.apply {
                threshold = if (threshold == null || threshold!! < 1) LOW_DEFAULT_MG.toFloat() else threshold
                alarmInterval = if (alarmInterval == null || alarmInterval!! < 1) INTERVAL_DEFAULT else alarmInterval
                style = if (style == null || style!! < 1) STYLE_DEFAULT else style
                sound = if (sound.isNullOrEmpty()) SOUND_DEFAULT.toString() else sound
            }
            highAlarm?.apply {
                threshold = if (threshold == null || threshold!! < 1) HIGH_DEFAULT_MG.toFloat() else threshold
                alarmInterval = if (alarmInterval == null || alarmInterval!! < 1) INTERVAL_DEFAULT else alarmInterval
                style = if (style == null || style!! < 1) STYLE_DEFAULT else style
                sound = if (sound.isNullOrEmpty()) SOUND_DEFAULT.toString() else sound
            }
            signalLostAlarm?.apply {
                duration = if (duration == null || duration!! < 1) SIGNAL_LOSE_DEFAULT else duration
                style = if (style == null || style!! < 1) STYLE_DEFAULT else style
                sound = if (sound.isNullOrEmpty()) SOUND_DEFAULT.toString() else sound
            }
        }
    }

    fun getDefaultAlarmSettings(): UserGlucoseAlarmSettingsData {
        val veryAlarm = GlucoseAlarmItemData(
            VERY_LOW_INTERVAL,
            true,
            SOUND_DEFAULT.toString(),
            STYLE_DEFAULT,
            VERY_LOW_MG.toFloat(),
            null
        )
        val lowAlarm = GlucoseAlarmItemData(
            INTERVAL_DEFAULT,
            true,
            SOUND_DEFAULT.toString(),
            STYLE_DEFAULT,
            70f,
            null
        )
        val highAlarm = GlucoseAlarmItemData(
            INTERVAL_DEFAULT,
            true,
            SOUND_DEFAULT.toString(),
            STYLE_DEFAULT,
            250f,
            null
        )
        val lose = GlucoseAlarmItemData(
            SIGNAL_LOSE_DEFAULT,
            true,
            SOUND_DEFAULT.toString(),
            STYLE_DEFAULT,
            null,
            SIGNAL_LOSE_DEFAULT
        )
        return UserGlucoseAlarmSettingsData(true, highAlarm, lowAlarm, lose, veryAlarm)
    }

    /**
     * 新用户默认值
     */
    fun getDefaultAlarmItem(item: GlucoseAlarmItemData, type: Int): GlucoseAlarmItemData {
        when (type) {
            GLUCOSE_LOW -> {
                item.apply {
                    if (alarmInterval == 0) {
                        item.alarmInterval = INTERVAL_DEFAULT
                    }
                    if (threshold != null && threshold!! < 0.5) {
                        item.threshold = LOW_MG[0].toFloat()
                    }
                }
            }

            GLUCOSE_HIGH -> {
                item.apply {
                    if (alarmInterval == 0) {
                        item.alarmInterval = INTERVAL_DEFAULT
                    }
                    if (threshold != null && threshold!! < 0.5) {
                        item.threshold = HIGH_MG[0].toFloat()
                    }
                }
            }

            SIGNAL_LOSS -> {
                item.apply {
                    if (duration == null || duration == 0) {
                        item.duration = SIGNAL_LOSE_DEFAULT
                    }
                }
            }
        }
        item.apply {
            if (style == 0) {
                style = STYLE_DEFAULT
            }
            if (sound.isNullOrEmpty() || sound == "0") {
                sound = SOUND_DEFAULT.toString()
            }
        }
        return item
    }

    fun getRemoteCompat(remote: SharerEntity?): UserGlucoseAlarmSettingsData? {
        var userAlarmData: UserGlucoseAlarmSettingsData? = null
        remote?.apply {
            //lower,upper 注册后没有开启,返回的是0.0
            //enable,forceRemind 注册后没有开启,返回的是false
            //style 注册后没有开启,返回的是0
            var highAlarm: GlucoseAlarmItemData? = null//高血糖提醒
            var lowAlarm: GlucoseAlarmItemData? = null//低血糖提醒
            var signalLostAlarm: GlucoseAlarmItemData? = null//信号丢失提醒
            var veryLowAlarm: GlucoseAlarmItemData? = null//极低血糖提醒
            if (alarm.isEnable) {
                highAlarm = GlucoseAlarmItemData(
                    INTERVAL_DEFAULT,
                    true,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    alarm.upper,
                    null
                )
                lowAlarm = GlucoseAlarmItemData(
                    INTERVAL_DEFAULT,
                    true,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    alarm.lower,
                    null
                )
                signalLostAlarm =
                    GlucoseAlarmItemData(
                        SIGNAL_LOSE_DEFAULT,
                        true,
                        alarm.sound ?: SOUND_DEFAULT.toString(),
                        alarm.style ?: STYLE_DEFAULT,
                        null,
                        SIGNAL_LOSE_DEFAULT
                    )
                //极低
                veryLowAlarm = GlucoseAlarmItemData(
                    VERY_LOW_INTERVAL,
                    true,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    VERY_LOW_MG.toFloat(),
                    null
                )
                userAlarmData = UserGlucoseAlarmSettingsData(
                    alarm.isForceRemind,
                    highAlarm,
                    lowAlarm,
                    signalLostAlarm,
                    veryLowAlarm
                )
            } else {
                highAlarm = GlucoseAlarmItemData(
                    INTERVAL_DEFAULT,
                    false,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    alarm.upper,
                    null
                )
                lowAlarm = GlucoseAlarmItemData(
                    INTERVAL_DEFAULT,
                    false,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    alarm.lower,
                    null
                )
                signalLostAlarm =
                    GlucoseAlarmItemData(
                        SIGNAL_LOSE_DEFAULT,
                        false,
                        alarm.sound ?: SOUND_DEFAULT.toString(),
                        alarm.style ?: STYLE_DEFAULT,
                        null,
                        SIGNAL_LOSE_DEFAULT
                    )
                //极低
                veryLowAlarm = GlucoseAlarmItemData(
                    VERY_LOW_INTERVAL,
                    true,
                    alarm.sound ?: SOUND_DEFAULT.toString(),
                    alarm.style ?: STYLE_DEFAULT,
                    VERY_LOW_MG.toFloat(),
                    null
                )
            }
            userAlarmData = UserGlucoseAlarmSettingsData(
                alarm.isForceRemind,
                highAlarm,
                lowAlarm,
                signalLostAlarm,
                veryLowAlarm
            )
        }
        return userAlarmData
    }

    /**
     * 旧用户信息字段兼容新提醒设置
     */
    fun getUserAlarmCompat(): UserGlucoseAlarmSettingsData {
        var userAlarmData: UserGlucoseAlarmSettingsData? = null
        UserInfoUtils.getPersonalInfo()?.alarm?.apply {
            //lower,upper 注册后没有开启,返回的是0.0
            //enable,forceRemind 注册后没有开启,返回的是false
            //style 注册后没有开启,返回的是0
            var highAlarm: GlucoseAlarmItemData? = null//高血糖提醒
            var lowAlarm: GlucoseAlarmItemData? = null//低血糖提醒
            var signalLostAlarm: GlucoseAlarmItemData? = null//信号丢失提醒
            var veryLowAlarm: GlucoseAlarmItemData? = null//极低血糖提醒
            if (isEnable) {
                highAlarm = GlucoseAlarmItemData(INTERVAL_DEFAULT, true, sound, style, upper, null)
                lowAlarm = GlucoseAlarmItemData(INTERVAL_DEFAULT, true, sound, style, lower, null)
                signalLostAlarm =
                    GlucoseAlarmItemData(
                        INTERVAL_DEFAULT,
                        true,
                        sound,
                        style,
                        null,
                        SIGNAL_LOSE_DEFAULT
                    )
            } else {
                highAlarm = GlucoseAlarmItemData(INTERVAL_DEFAULT, false, sound, style, upper, null)
                lowAlarm = GlucoseAlarmItemData(INTERVAL_DEFAULT, false, sound, style, lower, null)
                signalLostAlarm =
                    GlucoseAlarmItemData(INTERVAL_DEFAULT, false, sound, style, null, null)
                signalLostAlarm.duration = SIGNAL_LOSE_DEFAULT
            }
            //极低
            veryLowAlarm = GlucoseAlarmItemData(
                VERY_LOW_INTERVAL,
                true,
                sound,
                style,
                VERY_LOW_MG.toFloat(),
                SIGNAL_LOSE_DEFAULT
            )
            userAlarmData = UserGlucoseAlarmSettingsData(
                isForceRemind,
                highAlarm,
                lowAlarm,
                signalLostAlarm,
                veryLowAlarm
            )
        }
        return userAlarmData!!
    }

}