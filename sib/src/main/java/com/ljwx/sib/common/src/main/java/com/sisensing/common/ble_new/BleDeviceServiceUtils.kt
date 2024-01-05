package com.sisensing.common.ble_new

import com.sisensing.common.ble.BluetoothServiceModel

object BleDeviceServiceUtils {

    private val service by lazy { BluetoothServiceModel() }

    fun isNeedUploadGlucoseData(deviceName: String, deviceId: String) {
        service.getCurrentIndex(deviceName, deviceId, "")
    }

    fun updateDeviceInfo2Server(
        bluetoothNum: String,
        macAddress: String,
        deviceName: String,
        algorithmVersion: String
    ) {
        service.addDevice(bluetoothNum, macAddress, deviceName, algorithmVersion)
    }

}