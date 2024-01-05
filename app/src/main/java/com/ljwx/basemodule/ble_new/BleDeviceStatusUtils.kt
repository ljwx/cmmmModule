package com.sisensing.common.ble_new

import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
import com.sisensing.common.ble.BleLog.dGlucose
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.constants.Constant
import com.sisensing.common.entity.Device.DeviceRepository
import com.sisensing.common.utils.BroadcastManager

object BleDeviceStatusUtils {

    /**
     * 电流异常在一个小时内,算损坏
     */
    fun sensorIsDamagedInOneHour(index: Int, state: Int, userId: String): Boolean {
        // 电流报警  //0.正常  1.过低  2.过高
        if (index < 60 && state > 0) {
            //一个小时内的传感器异常(传感器损坏) 断开连接并更新状态为4
            sensorIsDamaged(userId)
            return true
        }
        return false
    }

    /**
     * 传感器过期
     */
    fun sensorIsExpire(
        userId: String,
        extra: String,
    ) {
        //传感器失效(已停用)断开连接并更新状态为2
        changeSensorBreakdownOrExpire(userId!!, 2, extra!!)
    }

    /**
     * 传感器损坏
     */
    fun sensorIsDamaged(userId: String) {
        //一个小时内的传感器异常(传感器损坏) 断开连接并更新状态为4
        changeSensorBreakdownOrExpire(userId, 4, Constant.SENSOR_EXCEPTION)
    }

    /**
     * 传感器损坏或过期
     */
    private fun changeSensorBreakdownOrExpire(
        userId: String,
        deviceStatus: Int,
        extra: String,
    ) {
        SensorDeviceInfo.sensorIsEdOrEp = true
        if (SensorDeviceInfo.isRightNowStopData) return

        //同步更新内存当中的设备状态
        if (SensorDeviceInfo.mDeviceEntity != null) {
            SensorDeviceInfo.mDeviceEntity.deviceStatus = deviceStatus
        }
        DeviceRepository.getInstance()
            .updateDeviceStatus(userId, SensorDeviceInfo.mDeviceName, deviceStatus, 0)
        BroadcastManager.getInstance(Utils.getApp())
            .sendBroadcast(Constant.SENSOR_EXCEPTION_AND_INVALID_BROAD_CAST, extra)
        BleLibUtils.stopConnect()
        if (SensorDeviceInfo.mDeviceEntity != null && ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName) && ObjectUtils.isNotEmpty(
                SensorDeviceInfo.mDeviceId
            )
        ) {
            dGlucose("设备异常或过期,若有未上传的血糖数据,则上传")
            BleDeviceServiceUtils.isNeedUploadGlucoseData(
                SensorDeviceInfo.mDeviceName,
                SensorDeviceInfo.mDeviceId
            )
        }
    }

}