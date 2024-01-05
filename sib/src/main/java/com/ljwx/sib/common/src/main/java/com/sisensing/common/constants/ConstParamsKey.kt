package com.sisensing.common.constants

object ConstParamsKey {

    /**
     * 通知用
     */
    //待处理的共享请求
    val SHARE_INVITE_PERSONAL = "toDoList"
    val SHARE_INVITE_INSTITUTION = "toDoInstitution"

    //血糖
    val FOLLOWER_BS_MONITOR = "toFollowerBsMonitor"
    val BS_MONITORING = "toMonitoring"

    //用户协议更新
    val PRIVACY_VERSION = "privacyVersion"

    //打卡actionId
    val CHECK_IN_ACTION_ID = "checkInActionId"
    val CHECK_IN_DATA_ID = "checkInDataId"

    @JvmStatic
    val BUNDLE_KEY = "arguments_bundle_key"

    val ALARM_FRIEND = "alarm_friend"

    @JvmStatic
    val EVENT_VERSION_COMPAT = "event_3_4"

}