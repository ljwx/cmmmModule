package com.sisensing.common.entity.notification

import com.ljwx.baseapp.extensions.isInt
import com.sisensing.common.utils.GlucoseAlarmUtils

data class FcmMessageBean(
    val code: Int?,
    val sound: String?,
    val forceRemind: Boolean?,
    val style: Int?,//提醒模式? //1:声音 2:震动 3:声音+震动
    val id: String?,//设备id?
    val eventType: Int?,//1:low 2:high 3:very 4:lose
    val nickName: String?,//亲友昵称
    val t: Long?, //时间
    val v: String?, //值
    val unit: String?, //单位
) {
    companion object {
        const val ALARM_LOW = 1
        const val ALARM_HIGH = 2
        const val ALARM_VERY = 3
        const val ALARM_LOST = 4
    }

    fun getSound(): Int? {
        if (sound != null) {
            if (sound.isInt()) {
                return sound.toInt()
            } else {
                return GlucoseAlarmUtils.SOUND_DEFAULT
            }
        } else {
            return GlucoseAlarmUtils.SOUND_DEFAULT
        }
    }

}