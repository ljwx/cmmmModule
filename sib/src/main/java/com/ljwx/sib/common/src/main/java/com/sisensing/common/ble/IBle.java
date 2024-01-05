package com.sisensing.common.ble;

import com.sisensing.common.entity.Device.DeviceEntity;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/8/17 15:16
 * Description:
 */
public interface IBle {


    void startConnect(DeviceEntity deviceEntity);

    void updateDevice(DeviceEntity deviceEntity);

    void stopConnect();

    boolean isSibBluetoothConnected();

    void releaseAlgorithmContext(String deviceName);

    void addConnectListener(CgmConnectListener connectListener);

    void removeConnectListener(CgmConnectListener connectListener);

    void setStatusListener(CgmStatusListener statusListener);
}
