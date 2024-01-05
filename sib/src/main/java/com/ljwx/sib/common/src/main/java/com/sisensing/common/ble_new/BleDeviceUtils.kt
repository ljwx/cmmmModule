package com.sisensing.common.ble_new

import com.blankj.utilcode.util.LogUtils
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.entity.Device.DeviceEntity
import com.sisensing.common.entity.Device.DeviceManager
import com.sisensing.common.utils.Log

object BleDeviceUtils {

    @JvmStatic
    fun updateLastDevice(deviceEntity: DeviceEntity) {
        Log.v("蓝牙:app自己方法", "updateDevice:更新deviceEntity")
        SensorDeviceInfo.mDeviceEntity = deviceEntity
        SensorDeviceInfo.mDeviceId = deviceEntity.getDeviceId()
        DeviceManager.getInstance().deviceEntity =
            SensorDeviceInfo.mDeviceEntity
        LogUtils.i("------------------------>updateDevice:" + deviceEntity.getDeviceName())
    }

}