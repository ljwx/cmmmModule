package com.ljwx.basemodule.ble.v4.extract;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.v4.data.GlucoseDataInfo;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.ble_new.BleLibUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceRepository;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.BroadcastManager;

abstract public class LocalBleService4SensorStateV4 extends LocalBleService3ReconnectionV4 {

    /**
     * 传感器损坏
     */
    protected void sensorIsDamaged(String userId, int index, int numOfUnreceived) {
        //一个小时内的传感器异常(传感器损坏) 断开连接并更新状态为4
        changeSensorBreakdownOrExpire(userId, 4, Constant.SENSOR_EXCEPTION, index, numOfUnreceived);
    }

    /**
     * 传感器过期
     */
    protected void sensorIsExpire(String userId, String extra, int index, int numOfUnreceived) {
        //传感器失效(已停用)断开连接并更新状态为2
        changeSensorBreakdownOrExpire(userId, 2, extra, index, numOfUnreceived);
    }

    /**
     * 传感器损坏或过期
     */
    private void changeSensorBreakdownOrExpire(String userId, int deviceStatus, String extra, int index, int numOfUnreceived) {
        SensorDeviceInfo.sensorIsEdOrEp = true;

        if (SensorDeviceInfo.isRightNowStopData) return;

        //同步更新内存当中的设备状态
        if( SensorDeviceInfo.mDeviceEntity != null){
            SensorDeviceInfo.mDeviceEntity.setDeviceStatus(deviceStatus);
        }
        DeviceRepository.getInstance().updateDeviceStatus(userId, SensorDeviceInfo.mDeviceName, deviceStatus, 0);
        BroadcastManager.getInstance(this).sendBroadcast(Constant.SENSOR_EXCEPTION_AND_INVALID_BROAD_CAST, extra);

        BleLibUtils.stopConnect();
        if (SensorDeviceInfo.mDeviceEntity != null && ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName) && ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceId)) {
            BleLog.dGlucose("设备异常或过期,若有未上传的血糖数据,则上传");
            mServiceModel.getCurrentIndex(SensorDeviceInfo.mDeviceName, SensorDeviceInfo.mDeviceId, SensorDeviceInfo.mDeviceEntity.getAlgorithmVersion());
        }
    }

    /**
     * 电流异常在一个小时内,算损坏
     */
    protected boolean sensorIsDamagedInOneHour(int index, int state, String userId) {
        // 电流报警  //0.正常  1.过低  2.过高
        if (index < 60 && state > 0) {
            //一个小时内的传感器异常(传感器损坏) 断开连接并更新状态为4
            sensorIsDamaged(userId, index, GlucoseDataInfo.mNumUnReceived);
            return true;
        }
        return false;
    }

    protected void setCurrentAlarmStatus(BloodGlucoseEntity glucoseEntity, String userId, String deviceName, int currentWarning, int index, int numOfUnreceived) {
        //用用户id+设备名作为存储名
        String mmkvName = userId + deviceName;
        //电流异常index
        int acIndex = UserInfoUtils.getAcIndex();
        //电流异常次数
        int acCount = UserInfoUtils.getAcCount();

        if (currentWarning > 0) {
            UserInfoUtils.putAcIndex(index);
            UserInfoUtils.putAcCount(acCount + 1);
            LogUtils.e("acIndex1,acCount1", acIndex, acCount);
            // TODO: 2021/6/1 30分钟的传感器异常,更新alarmStatus为2
            glucoseEntity.setAlarmStatus(2);
            if (acCount >= 180) {
                //3小时候的传感器异常
                // TODO: 2021/6/1 deviceStatus为4
                //更新本地设备deviceStatus为4 断开传感器
                sensorIsDamaged(userId, index, numOfUnreceived);
            }
        } else {
            if (acIndex != 0 && index <= acIndex + 30) {
                //传感器异常且30分钟之内
                UserInfoUtils.putAcCount(acCount + 1);
                // TODO: 2021/6/1 更新alarmStatus为2
                glucoseEntity.setAlarmStatus(2);
                if (acCount >= 180) {
                    //3小时候的传感器异常
                    // TODO: 2021/6/1
                    //更新本地设备deviceStatus为4
                    sensorIsDamaged(userId, index, numOfUnreceived);
                }
            } else {
                //恢复正常
                // TODO: 2021/6/1 判定温度异常
                // TODO: 2021/6/1 判定血糖偏高/偏低 (只能判定有血糖值)
                // TODO: 2021/6/1 更新alarmStatus
                UserInfoUtils.putAcIndex(0);
                UserInfoUtils.putAcCount(0);
            }
        }
    }

}
