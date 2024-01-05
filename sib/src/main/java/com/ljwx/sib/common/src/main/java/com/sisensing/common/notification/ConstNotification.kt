package com.sisensing.common.notification

import com.sisensing.common.R

object ConstNotification {

    //数据key
    @JvmStatic
    val FCM_DATA_KEY = "sijoy.data"

    //血糖报警
    val TYPE_SELF_ALARM = 1001

    //好友血糖报警
    val TYPE_FRIEND_ALARM = 1002

    //邀请
    val TYPE_SHARE_INVITE = 1003

    //机构邀请
    val TYPE_SHARE_INVITE_INSTITUTION = 1004

    //默认通知渠道
    val NORMAL_CHANNEL_ID = "default"

    val NO_FORCE_TAG = "_no_force"

    /**
     * 渠道和铃声对应关系
     */
    val SOUNDS_FORCE_MAP = mapOf(
        "sound0.mp3" to R.raw.sound1,
        "sound1.mp3" to R.raw.sound1,
        "sound2.mp3" to R.raw.sound2,
        "sound3.mp3" to R.raw.sound3,
        "sound4.mp3" to R.raw.sound4,
        "sound5.mp3" to R.raw.sound5,
        "sound6.mp3" to R.raw.sound6,
        "sound7.mp3" to R.raw.sound7,
        "sound8.mp3" to R.raw.sound8,
        "sound9.mp3" to R.raw.sound9,
        "sound10.mp3" to R.raw.sound10,
    )

    val SOUND_NORMAL_MAP = mapOf(
        "sound0.mp3$NO_FORCE_TAG" to R.raw.sound1,
        "sound1.mp3$NO_FORCE_TAG" to R.raw.sound1,
        "sound2.mp3$NO_FORCE_TAG" to R.raw.sound2,
        "sound3.mp3$NO_FORCE_TAG" to R.raw.sound3,
        "sound4.mp3$NO_FORCE_TAG" to R.raw.sound4,
        "sound5.mp3$NO_FORCE_TAG" to R.raw.sound5,
        "sound6.mp3$NO_FORCE_TAG" to R.raw.sound6,
        "sound7.mp3$NO_FORCE_TAG" to R.raw.sound7,
        "sound8.mp3$NO_FORCE_TAG" to R.raw.sound8,
        "sound9.mp3$NO_FORCE_TAG" to R.raw.sound9,
        "sound10.mp3$NO_FORCE_TAG" to R.raw.sound10,
        "normal" to null,
    )


    /**
     * 根据后端标志匹配通知渠道
     */
    fun getFcmChannelId(flag: String): String {
        if (SOUNDS_FORCE_MAP.containsKey(flag)) {
            return flag
        }
        if (SOUND_NORMAL_MAP.containsKey(flag)) {
            return flag
        }
        return NORMAL_CHANNEL_ID
    }

}