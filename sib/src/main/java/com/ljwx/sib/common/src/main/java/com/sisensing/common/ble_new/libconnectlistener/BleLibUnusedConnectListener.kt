package com.sisensing.common.ble_new.libconnectlistener

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import com.blankj.utilcode.util.PermissionUtils
import com.sisensing.common.ble.BleLog
import com.sisensing.common.share.LogUploadModel
import no.chuangan.android.support.v18.scanner.ScanResult
import no.sisense.android.callback.ConnectListener

abstract class BleLibUnusedConnectListener : ConnectListener {

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
    override fun onServiceConnected(componentName: ComponentName?) {
        BleLog.dLib("蓝牙库服务已连接")
    }

    override fun onDataRecive(bluetoothDevice: BluetoothDevice?, bytes: ByteArray?) {}

    override fun onDataRecive(bytes: ByteArray?) {}

    override fun <T> onGJDataRecive(bluetoothDevice: BluetoothDevice?, s: String?, t: T, i: Int) {
        BleLog.dLib("读取血糖、乳酸、血酮加密数据,加密")
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
    override fun onConnectLog(s: String) {
        LogUploadModel.getInstance().uploadConnectInfo(s)
        BleLog.dLibLog("日志:$s")
    }

    override fun onScanMatchDevice(scanResult: ScanResult?) {}


    override fun onBatteryValueReceived(bluetoothDevice: BluetoothDevice, i: Int) {
        BleLog.dLib("设备电量回调," + getDeviceName(bluetoothDevice) + ": " + i)
    }

    protected open fun getDeviceName(bluetoothDevice: BluetoothDevice): String {
        if (PermissionUtils.isGranted(Manifest.permission.BLUETOOTH_CONNECT)) {
            return bluetoothDevice.name
        } else {
            return ""
        }
    }

    override fun onServicesDiscovered(bluetoothDevice: BluetoothDevice, b: Boolean) {
        BleLog.dLib("onServicesDiscovered回调," + getDeviceName(bluetoothDevice))
    }

    override fun receiveBatteeryPercentageChage(bluetoothDevice: BluetoothDevice?, i: Int) {}

    override fun CGMCmdDoReply(bluetoothDevice: BluetoothDevice, i: Int, i1: Int, i2: Int) {
        BleLog.dLib("命令执行结果回调," + getDeviceName(bluetoothDevice) + ",是否成功:" + (i1 == 1))
    }

    override fun CGMCmdDoReplyV110(bluetoothDevice: BluetoothDevice, i: Int, i1: Int) {
        BleLog.dLib("命令执行结果回调V110," + getDeviceName(bluetoothDevice) + ",是否成功:" + (i1 == 1))
    }

    override fun onBatchScanResultsB(list: List<ScanResult?>?) {}

}