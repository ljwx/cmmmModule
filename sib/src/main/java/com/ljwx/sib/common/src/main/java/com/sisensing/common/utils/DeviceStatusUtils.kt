package com.sisensing.common.utils

object DeviceStatusUtils {

    //设备状态 0：未启用(当前没有) 1.使用中 2已停用(失效) 3.初始化 4.异常(传感器损坏)
    private const val REMOTE_UNUSED = 0
    private const val REMOTE_IN_USE = 1
    private const val REMOTE_EXPIRE = 2
    private const val REMOTE_INITIALIZING = 3
    private const val REMOTE_EXCEPTION = 4

    //1.正常 2.半小时的电流异常 3.温度过高 4.温度过低 5.血糖过高 6.血糖过低
    private const val HALF_HOUR_CURRENT_EXCEPTION = 2
    private const val TEMPERATURE_HIGH = 3
    private const val TEMPERATURE_LOW = 4

    /**
     * 服务端获取的状态是否为使用中
     */
    @JvmStatic
    fun isInUseFromServer(status: Int?): Boolean {
        return status == REMOTE_IN_USE || status == REMOTE_INITIALIZING
    }

    /**
     * 血糖数据中,设备状态是否异常
     */
    @JvmStatic
    fun glucoseAlarmIsException(alarmStatus: Int?): Boolean {
        return alarmStatus == HALF_HOUR_CURRENT_EXCEPTION || alarmStatus == TEMPERATURE_HIGH || alarmStatus == TEMPERATURE_LOW
    }

}