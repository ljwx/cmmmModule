package com.sisensing.common.ble.v4.extract;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.LogUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.CgmConnectListener;
import com.sisensing.common.ble.IBle;
import com.sisensing.common.ble.area.DeviceAreaCodeUtils;
import com.sisensing.common.ble.v4.AppBleStateBroadcastReceiver;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.utils.BroadcastManager;
import com.sisensing.common.utils.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class LocalBleService1BleState extends Service implements IBle {

    public static final int BT_STATE_OPEN = 1;
    public static final int BT_STATE_UNKNOWN = 2;
    public static final int BT_STATE_CLOSE = -1;
    public static int BT_STATE = BT_STATE_UNKNOWN;

    /**
     * 蓝牙开关广播监听
     */
    private final BroadcastReceiver blueToothStateReceiver = new AppBleStateBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF://蓝牙已关闭
                        Log.d("蓝牙:系统广播", "蓝牙关闭");
                        if (!stateOffAndCouldOpen(LocalBleService1BleState.this)) {
                            Log.d("蓝牙:系统广播", "设备名不为空:通知监听者断开连接");
                            disConnected(true);
                        }
                        break;
                    case BluetoothAdapter.STATE_ON://蓝牙已开启
                        Log.d("蓝牙:系统广播", "蓝牙开启");
                        BleLog.dReconnect("蓝牙状态改变为开启,发起重连");
                        resendReconnection();
                        break;
                    default:
                        Log.d("蓝牙:系统广播", "蓝牙未知状态");
                        Log.e("BlueToothError", "蓝牙状态未知");
                }
            }
        }
    };

    protected List<CgmConnectListener> mConnectListener = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        registerBleStateBroadcast();
    }

    private void registerBleStateBroadcast() {
        IntentFilter bleEnableFilter = new IntentFilter();
        bleEnableFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(blueToothStateReceiver, bleEnableFilter);
    }

    protected abstract void connectBluetooth();

    protected abstract void resendReconnection();

    @Override
    public void updateDevice(DeviceEntity deviceEntity) {
        Log.v("蓝牙:app自己方法", "updateDevice:更新deviceEntity");
        SensorDeviceInfo.mDeviceEntity = deviceEntity;
        SensorDeviceInfo.mDeviceId = deviceEntity.getDeviceId();
        DeviceManager.getInstance().setDeviceEntity(SensorDeviceInfo.mDeviceEntity);
        LogUtils.i("------------------------>updateDevice:" + deviceEntity.getDeviceName());
    }

    protected void disConnected(boolean showToast) {
        Log.d("蓝牙", "各种原因,通知监听者未连接");
        for (CgmConnectListener connectListener : mConnectListener) {
            connectListener.disConnected(showToast);
        }
    }


    protected void connecting() {
        Log.d("蓝牙", "各种原因,通知监听者连接中");
        for (CgmConnectListener connectListener : mConnectListener) {
            connectListener.connecting();
        }
    }

    protected void areaIsValid(String content) {
        if (!DeviceAreaCodeUtils.isValid(content)) {
            for (CgmConnectListener connectListener : mConnectListener) {
                String className = connectListener.getClass().getSimpleName();
                android.util.Log.d("区域限制", "不可用回调:"+className);
                if (mConnectListener.size() > 1 && className.equals("BsMonitoringViewModel")) {
                    continue;
                }
                connectListener.areaValid(false, DeviceAreaCodeUtils.getAreaCode(content));
            }
        }
    }

    protected void connected() {
        Log.d("蓝牙", "各种原因,通知监听者连接成功");
        for (CgmConnectListener connectListener : mConnectListener) {
            connectListener.connected();
        }
    }

    @Override
    public void addConnectListener(CgmConnectListener connectListener) {
        Log.d("蓝牙:app自己方法", "addConnectListener:添加监听者");
        mConnectListener.add(connectListener);
    }


    @Override
    public void removeConnectListener(CgmConnectListener connectListener) {
        Log.d("蓝牙:app自己方法", "removeConnectListener:移除监听者");
        mConnectListener.remove(connectListener);
    }

    @Override
    public void onDestroy() {
        BleLog.eApp("App蓝牙服务马上onDestroy,调用停止连接,取消蓝牙状态的监听");
        stopConnect();
        unregisterReceiver(blueToothStateReceiver);
        BroadcastManager.getInstance(this).destroy(Constant.BROADCAST_INIT_DEVICE);
        super.onDestroy();
    }

}
