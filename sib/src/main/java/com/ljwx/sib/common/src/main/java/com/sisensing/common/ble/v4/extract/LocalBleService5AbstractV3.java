package com.sisensing.common.ble.v4.extract;

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
import com.blankj.utilcode.util.ThreadUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.notification.NotificationClickReceiver;
import com.sisensing.common.notification.NotificationConfig;
import com.sisensing.common.notification.NotificationUtils;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.utils.Log;

import java.util.List;

import no.chuangan.android.support.v18.scanner.ScanResult;

import no.sisense.android.callback.ConnectListener;

public abstract class LocalBleService5AbstractV3 extends LocalBleService4SensorState implements ConnectListener {

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
        public LocalBleService5AbstractV3 getService() {
            return LocalBleService5AbstractV3.this;
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

//    @Override
//    public void onScanFailed(int i, String s) {
//        Log.d("蓝牙:jar包回调", "onScanFailed:扫描失败");
//        LogUploadModel.getInstance().uploadConnectInfo("onScanFailed:i=" + i + ",s=" + s);
//    }

//    @Override
//    public void onServiceBound(CGMService.CGMSBinder cgmsBinder) {
//        Log.d("蓝牙:jar包回调", "onServiceBound:");
//        LogUtils.e("onServiceBound：" + cgmsBinder.getDeviceName() + "------------>" + cgmsBinder.getDeviceAddress());
//    }

    @Override
    public void onServiceConnected(ComponentName componentName) {
        BleLog.dLib("onServiceConnected:服务已连接");
        LogUtils.e("onServiceConnected：");
    }

    @Override
    public void onDataRecive(BluetoothDevice bluetoothDevice, byte[] bytes) {

    }

    @Override
    public void onDataRecive(byte[] bytes) {

    }

    @Override
    public <T> void onGJDataRecive(BluetoothDevice bluetoothDevice, String s, T t, int i) {
        BleLog.dLib("读取血糖、乳酸、血酮加密数据,加密");
    }

//    @Override
//    public void onBroadcatNewCGMValue(BluetoothDevice bluetoothDevice, CGMRecord cgmRecord) {
//        LogUtils.e("onBroadcatNewCGMValue：" + bluetoothDevice.getName() + "---------------->" + bluetoothDevice.getAddress());
//    }
//
//    @Override
//    public void onBroadcastError(BluetoothDevice bluetoothDevice, String s, int i) {
//        LogUtils.e("onBroadcastError：" + bluetoothDevice.getName() + "---------------->" + bluetoothDevice.getAddress() + "--------->" + s);
//        LogUploadModel.getInstance().uploadConnectInfo("onBroadcastError:i=" + i + ",s=" + s);
//    }

    @Override
    public void onConnectLog(String s) {
        LogUtils.i("onConnectLog：" + s);

        LogUploadModel.getInstance().uploadConnectInfo(s);
        BleLog.dLibLog("日志:" + s);
    }

    @Override
    public void onScanMatchDevice(ScanResult scanResult) {

    }


    @Override
    public void onBatteryValueReceived(BluetoothDevice bluetoothDevice, int i) {
        BleLog.dLib("设备电量回调,"+getDeviceName(bluetoothDevice)+": "+i);
    }

    protected String getDeviceName(BluetoothDevice bluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return bluetoothDevice.getName();
        }
        return "没有权限获取蓝牙名称";
    }

    @Override
    public void onServicesDiscovered(BluetoothDevice bluetoothDevice, boolean b) {
        BleLog.dLib("onServicesDiscovered回调," + getDeviceName(bluetoothDevice));
    }

    @Override
    public void receiveBatteeryPercentageChage(BluetoothDevice bluetoothDevice, int i) {

    }

    @Override
    public void CGMCmdDoReply(BluetoothDevice bluetoothDevice, int i, int i1, int i2) {
        BleLog.dLib("命令执行结果回调," + getDeviceName(bluetoothDevice) + ",是否成功:" + (i1 == 1));
    }

    @Override
    public void CGMCmdDoReplyV110(BluetoothDevice bluetoothDevice, int i, int i1) {
        BleLog.dLib("命令执行结果回调V110," + getDeviceName(bluetoothDevice) + ",是否成功:" + (i1 == 1));
    }

    @Override
    public void onBatchScanResultsB(List<ScanResult> list) {

    }

}
