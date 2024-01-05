package com.ljwx.basemodule.ble.v4.extract;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.BluetoothServiceModel;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.utils.BroadcastManager;

abstract public class LocalBleService2ModelV4 extends LocalBleService1BleStateV4 {

    protected BluetoothServiceModel mServiceModel;

    @Override
    public void onCreate() {
        super.onCreate();
        BleLog.eApp("App蓝牙服务onCreate");
        mServiceModel = new BluetoothServiceModel();
        registerAppLaunchBroadcast();
    }


    private void registerAppLaunchBroadcast() {
        //app启动后就开始接收此广播来调用一些需要启动就要调的接口
        BroadcastManager.getInstance(this).addAction(Constant.BROADCAST_INIT_DEVICE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BleLog.eApp("收到app启动广播,从数据库通过用户id查询设备信息");
                DeviceEntity entity = DeviceManager.getInstance().getDeviceEntity();
                if (entity != null && ObjectUtils.isNotEmpty(entity.getDeviceName()) && ObjectUtils.isNotEmpty(entity.getDeviceId())) {
                    BleLog.dGlucose("设备名,设备id都不为空,则上传行为数据,上传血糖数据");
                    String deviceName = entity.getDeviceName();
                    String deviceId = entity.getDeviceId();
                    mServiceModel.getClockInData(deviceId);
                    mServiceModel.getCurrentIndex(deviceName, deviceId, entity.getAlgorithmVersion());
                }

            }
        });
    }

}
