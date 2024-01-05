package com.ljwx.basemodule.ble;

import android.os.Handler;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.algorithom.AlgorithmFactory;
import com.sisensing.common.algorithom.IAlgorithm;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.ble.v4.extract.LocalBleService5AbstractV4;
import com.sisensing.common.ble_new.BleDeviceConnectUtils;
import com.sisensing.common.ble_new.BleDeviceGlucoseDataUtils;
import com.sisensing.common.ble_new.BleLibUtils;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.Log;

public class LocalBleServiceV4 extends LocalBleService5AbstractV4 {

    private IAlgorithm mIAlgorithm;

    private CgmStatusListener mStatusListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("蓝牙", "初始化jar包蓝牙");
        BleDeviceGlucoseDataUtils.setStatusListener(mStatusListener);
        BleLibUtils.init(this);
    }

    /**
     * deviceName: 不能为空,需要连接的BLE设备名称
     * delayMillis: 不能为空,首次连接或者异常重新连接后,执行扫描的最长时间
     * mCallback: 不能为空,回调接口,连接状态、数据传输、连接异常等信息
     * Action：  不能为空, 2--开始连接；1--停止连接
     *
     * @param
     */
    @Override
    public void startConnect(DeviceEntity deviceEntity) {
        BleLog.dApp("startConnect:开始连接,更新deviceEntity,deviceName:" + deviceEntity.getDeviceName());
        SensorDeviceInfo.isRightNowStopData = false;
        SensorDeviceInfo.mDeviceEntity = deviceEntity;
        SensorDeviceInfo.mDeviceId = deviceEntity.getDeviceId();

        mReconnectMills = mReconnectMillsUnlock;
        if (BleLibUtils.isInitialized()) {
            BleDeviceConnectUtils.prepareDataAndLibConnectDevice();
        } else {
            BleLibUtils.init(this);
            new Handler().postDelayed(() -> BleDeviceConnectUtils.prepareDataAndLibConnectDevice(), 1000);
        }
    }

    @Override
    public void releaseAlgorithmContext(String deviceName) {
        BleLog.dApp("releaseAlgorithmContext:准备算法?");
        if (mIAlgorithm == null) {
            mIAlgorithm = AlgorithmFactory.createAlgorithm(DeviceManager.getInstance().getDeviceEntity().getAlgorithmVersion());
        }
        if (ObjectUtils.isNotEmpty(deviceName)) {
            mIAlgorithm.releaseAlgorithmContext(UserInfoUtils.getUserId(), deviceName);
        }
        BleLog.dApp("releaseAlgorithmContext:将DeviceName置为空字符串");
        SensorDeviceInfo.mDeviceName = "";

    }

    @Override
    public void setStatusListener(CgmStatusListener statusListener) {
        BleLog.dApp("setStatusListener:设置状态监听-" + statusListener.getClass().getSimpleName());
        mStatusListener = statusListener;
    }

    /**
     * @return 获取蓝牙连接状态
     */
    public int getConnectStatus() {
        return CURRENT_CONNECT_STATUS;
    }

    @Override
    public void onDestroy() {
        BleLog.eApp("APP蓝牙Service被销毁");
        super.onDestroy();
        BleLibUtils.removeListener();
    }
}
