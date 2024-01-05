package com.ljwx.basemodule.ble.v4.extract;

import android.Manifest;
import android.app.Notification;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.LogUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.notification.NotificationClickReceiver;
import com.sisensing.common.notification.NotificationConfig;
import com.sisensing.common.notification.NotificationUtils;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.utils.Log;

import java.util.List;

import no.chuangan.android.support.v18.scanner.ScanResult;
import no.sisense.android.callback.ConnectListener;

public abstract class LocalBleService5AbstractV4 extends LocalBleService4SensorStateV4{

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }


    public class LocalBinder extends Binder {
        public LocalBleService5AbstractV4 getService() {
            return LocalBleService5AbstractV4.this;
        }
    }

    /**
     * 初始化通知服务
     */
    private void initNotification() {
        Log.d("蓝牙", "初始化前台通知:Continuous Glucose Monitoring System");
        //启用前台服务，提升优先级
        Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
        intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
        Notification notification = NotificationUtils.createNotification(this, getString(NotificationConfig.TITLE), getString(NotificationConfig.CONTENT), NotificationConfig.DEF_ICONS, intent);
        startForeground(NotificationConfig.FOREGROUD_NOTIFICATION_ID, notification);
    }

}
