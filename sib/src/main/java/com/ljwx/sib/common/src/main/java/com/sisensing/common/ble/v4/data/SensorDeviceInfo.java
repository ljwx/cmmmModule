package com.sisensing.common.ble.v4.data;

import android.bluetooth.BluetoothDevice;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.algorithom.IAlgorithm;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.entity.Device.DeviceRepository;
import com.sisensing.common.utils.Log;

import no.sisense.android.bean.GjCGMRecord;

public class SensorDeviceInfo {

    //系统蓝牙设备
    public static BluetoothDevice mBluetoothDevice;

    //数据库设备类
    public static DeviceEntity mDeviceEntity;

    //传感器是否过期或者损坏
    public static boolean sensorIsEdOrEp = false;

    //ble设备地址
    public static String mMacAddress;

    //ble设备名称
    public static String mDeviceName;

    //传感器id?
    public static String mDeviceId;

    //是否立刻终止发送数据(主要是为了解决当异常情况下断开设备连接需要即时中断数据发送到主界面)
    public static boolean isRightNowStopData = false;

    //是否主动断开连接
    public static boolean isAcDis = false;

    private static boolean isNewConnected = false;

    /**
     * 传感器信息已确认
     */
    public static boolean sensorInfoSure() {
        boolean check = mDeviceEntity != null && ObjectUtils.isNotEmpty(mDeviceName) && ObjectUtils.isNotEmpty(mDeviceId);
        BleLog.dGlucose("当前血糖是否满足上传:" + check + ",设备信息是否正常:deviceEntity,deviceName,deviceId");
        return check;
    }

    /**
     * 传感器正常可用
     */
    public static boolean deviceInfoExistAndEnable() {
        return mDeviceEntity != null && !sensorIsEdOrEp;
    }

    /**
     * 是否是未连接过的设备
     */
    public static boolean newSensor(String deviceName) {
        return ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceName) || !SensorDeviceInfo.mDeviceName.equals(deviceName);
    }

    /**
     * 设备已连接,添加设备信息
     */
    public static void addDevice(BluetoothDevice bluetoothDevice, IAlgorithm mIAlgorithm) {
        addDevice(bluetoothDevice, mIAlgorithm, "");
    }

    public static void addDevice(BluetoothDevice bluetoothDevice, IAlgorithm mIAlgorithm, String characteristic) {
        mMacAddress = bluetoothDevice.getAddress();
        mDeviceName = bluetoothDevice.getName();
        Log.d("蓝牙:jar包回调", "蓝牙正常,设置DeviceEntity,userId:" + mDeviceEntity.getUserId() + "-设备名:" + mDeviceName);

        String algorithmVersion = mIAlgorithm.getAlgorithmVersion();

        mDeviceEntity.setAlgorithmVersion(algorithmVersion);
        mDeviceEntity.setMacAddress(mMacAddress);
        mDeviceEntity.setDeviceName(mDeviceName);

        mDeviceEntity.setConnectMill(System.currentTimeMillis());

        mDeviceEntity.setCharacteristic(characteristic);

        DeviceManager.getInstance().setDeviceEntity(mDeviceEntity);
        isNewConnected = true;
        Log.d("蓝牙", "新设备连接上");
    }

    /**
     * 设备已连接过,更新连接时间
     */
    public static boolean isConnectedDeviceAndUpdateTime() {
        Log.d("蓝牙:是否连接过的设备", "DeviceEntity是否为空:" + (mDeviceEntity == null)
                + "-deviceId:" + mDeviceId);
        if (!ObjectUtils.isEmpty(mDeviceId)) {
            //更新设备连接时间
            Log.d("蓝牙:jar包回调", "旧设备,数据库更新设备");
            DeviceRepository.getInstance().updateConnectMill(mDeviceName, System.currentTimeMillis());
            LogUtils.e("------------------------>更新设备时间" + mDeviceEntity.getDeviceName());
            return true;
        }
        return false;
    }

    /**
     * 传感器未过期,且没有异常情况
     */
    public static boolean notExpiredAndEnable(int index) {
        return index <= 20160 && !isRightNowStopData;
    }

    /**
     * 传感器正常
     */
    public static boolean notException() {
        return !SensorDeviceInfo.sensorIsEdOrEp && !SensorDeviceInfo.isRightNowStopData;
    }

    /**
     * 判断传感器状态
     */
    public static void judgeSensorEnable() {
        if (sensorIsEdOrEp) {
            isRightNowStopData = true;
        }
    }

    /**
     * 是否可以重连
     */
    public static boolean couldReconnection() {
        return !isAcDis && !SensorDeviceInfo.sensorIsEdOrEp
                && ObjectUtils.isNotEmpty(mDeviceName)
                && !SensorDeviceInfo.isRightNowStopData;
    }


    public static void requestRemoteGlucoseData(GjCGMRecord record) {
        if (!isNewConnected) {
            return;
        }
        Log.d("AGP", "是新设备");
//        RemoteGlucoseUtils.INSTANCE.needRequest(DailyTrendConst.NORMAL_DAYS);
        isNewConnected = false;
    }

}
