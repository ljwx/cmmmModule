package com.ljwx.basemodule.ble.v4.extract;//package com.sisensing.common.ble.v4.extract;
//
//import android.app.Notification;
//import android.bluetooth.BluetoothDevice;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.os.Binder;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//import com.blankj.utilcode.util.LogUtils;
//import com.sisensing.common.notification.NotificationClickReceiver;
//import com.sisensing.common.notification.NotificationConfig;
//import com.sisensing.common.notification.NotificationUtils;
//import com.sisensing.common.share.LogUploadModel;
//import com.sisensing.common.utils.Log;
//
//import java.util.ArrayList;
//
//import no.nordicsemi.android.support.v18.scanner.ScanResult;
//import no.sisense.android.api.CGMService;
//import no.sisense.android.bean.CGMRecord;
//import no.sisense.android.bean.GjCGMRecord;
//import no.sisense.android.callback.ConnectListener;
//
//public abstract class LocalBleService5Abstract extends LocalBleService4SensorState implements ConnectListener {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        initNotification();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    /**
//     * 初始化通知服务
//     */
//    private void initNotification() {
//        Log.d("蓝牙", "初始化前台通知:Continuous Glucose Monitoring System");
//        //启用前台服务，提升优先级
//        Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
//        intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
//        Notification notification = NotificationUtils.createNotification(this, getString(NotificationConfig.TITLE), getString(NotificationConfig.CONTENT), NotificationConfig.DEF_ICONS, intent);
//        startForeground(NotificationConfig.FOREGROUD_NOTIFICATION_ID, notification);
//    }
//
//    @Override
//    public void onScanFailed(int i, String s) {
//        Log.d("蓝牙:jar包回调", "onScanFailed:扫描失败");
//        LogUploadModel.getInstance().uploadConnectInfo("onScanFailed:i=" + i + ",s=" + s);
//    }
//
//    @Override
//    public void onServiceBound(CGMService.CGMSBinder cgmsBinder) {
//        Log.d("蓝牙:jar包回调", "onServiceBound:");
//        LogUtils.e("onServiceBound：" + cgmsBinder.getDeviceName() + "------------>" + cgmsBinder.getDeviceAddress());
//    }
//
//    @Override
//    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//        Log.d("蓝牙:jar包回调", "onServiceConnected:服务已连接");
//        LogUtils.e("onServiceConnected：");
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName componentName) {
//        Log.d("蓝牙:jar包回调", "onServiceDisconnected:服务断开");
//        LogUtils.i("onServiceDisconnected：" + componentName);
//    }
//
//    @Override
//    public void onDataRecive(ArrayList<GjCGMRecord> arrayList) {
//
//    }
//
//    @Override
//    public void onDataRecive(byte[] bytes) {
//
//    }
//
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
//
//    @Override
//    public void onConnectLog(String s) {
//        LogUtils.i("onConnectLog：" + s);
//
//        LogUploadModel.getInstance().uploadConnectInfo(s);
//
//    }
//
//    @Override
//    public void onScanMatchDevice(ScanResult scanResult) {
//
//    }
//
//}
