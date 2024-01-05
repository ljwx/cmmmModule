package com.sisensing.common.ble_new.status

object BleLibConnectStatus {

    //未连接
    const val DISCONNECTED = 0

    //已连接
    const val CONNECTED = 1

    //连接中
    const val CONNECTING = 2

    //断开中
    const val DISCONNECTING = 3

    //扫描中
    const val SCANNING = 4

    //鉴权失败
    const val AUTHENTICATION_FAIL = 5

    //鉴权成功
    const val AUTHENTICATION_SUCCESS = 6

}