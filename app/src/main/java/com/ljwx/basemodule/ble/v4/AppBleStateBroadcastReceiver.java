package com.ljwx.basemodule.ble.v4;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.BleUtils;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.DebugFileUtils;
import com.sisensing.common.utils.Log;

public class AppBleStateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
                    Log.d("蓝牙:系统广播", "蓝牙正在打开");
                    LogUtils.e("蓝牙正在打开");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭
                    Log.d("蓝牙:系统广播", "蓝牙正在关闭");
                    LogUtils.e("蓝牙正在关闭");
                    break;
            }
        }
    }

    protected boolean stateOffAndCouldOpen(Context context) {
        BleLog.dApp("蓝牙关闭,isDeviceConnected赋值为false");
        UserInfoUtils.isDeviceConnected = false;
        DebugFileUtils.setAppendFile("监测到蓝牙关闭：bleUtils is enable" + BleUtils.getInstance().isBleEnabled() + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----系统休眠时间：" + ScreenUtils.getSleepDuration());
        if (ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceName)) {
            Log.d("蓝牙:系统广播", "设备名为空:去开启蓝牙");
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                BleUtils.getInstance().openBle();
            }
            return true;
        }
        return false;
    }

}
