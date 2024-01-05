package com.sisensing.common.ble_new.status

object SensorDeviceStatus {

    //设备状态 0：未启用(当前没有) 1.使用中 2已停用(失效) 3.初始化 4.异常(传感器损坏)未知

    //未连接,未知状态
    const val NOT_CONNECT_UNKNOWN = -1
    const val NOT_ENABLED = 0
    const val IN_USE = 1
    const val EXPIRED = 2
    const val INITIALIZATION = 3
    const val EXCEPTION = 4

}