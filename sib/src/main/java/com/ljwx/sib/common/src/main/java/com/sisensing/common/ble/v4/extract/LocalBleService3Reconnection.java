package com.sisensing.common.ble.v4.extract;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.BleUtils;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.DebugFileUtils;
import com.sisensing.common.utils.Log;

abstract public class LocalBleService3Reconnection extends LocalBleService2Model {


    protected long mReconnectMillsUnlock = 1500;
    protected long mReconnectMillsLock = 5 * 60000;
    protected long mReconnectMills = mReconnectMillsUnlock;
    //
    private final int MSG_RECONNECT_OLD = 100000;
    protected static final int MSG_RECONNECT = 1000;

    protected int retryUploadLogTimes = 0;

    //当前连接状态,给个默认值为断开连接
    protected int CURRENT_CONNECT_STATUS = no.sisense.android.api.Constant.STATE_DISCONNECTED;

    protected Handler mReconnectionHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
//                case MSG_RECONNECT_OLD:
//                    Log.d("蓝牙", "handle收到message,马上重连");
//                    Log.d("上报日志", "handler发起重连,");
//                    //蓝牙关闭最多重试三次
//                    if (LocalBleService1BleState.BT_STATE == LocalBleService1BleState.BT_STATE_CLOSE) {
//                        if (retryUploadLogTimes < 3) {
//                            disConnectReConnect();
//                            retryUploadLogTimes += 1;
//                        }
//                    } else {
//                        disConnectReConnect();
//                    }
//                    break;
                case MSG_RECONNECT:
                    LogUtils.iTag("执行到重连，当前状态：",CURRENT_CONNECT_STATUS);
                    BleLog.dReconnect("handler接收到重连消息,下面还有判断是否真的需要重连的逻辑", true);
                    if (CURRENT_CONNECT_STATUS == no.sisense.android.api.Constant.STATE_CONNECTING ||
                            CURRENT_CONNECT_STATUS == no.sisense.android.api.Constant.STATE_SCANNING
                            || CURRENT_CONNECT_STATUS == no.sisense.android.api.Constant.STATE_CONNECTED){
                        BleLog.dReconnect("当前连接状态为连接中,或扫描中,或已连接,不执行重连");
                        return;
                    }
                    boolean isReConnect = BleUtils.getInstance().isBleEnabled()
                            && !isSibBluetoothConnected()
                            && !SensorDeviceInfo.isAcDis
                            && !SensorDeviceInfo.sensorIsEdOrEp
                            && !SensorDeviceInfo.isRightNowStopData
                            && UserInfoUtils.isLogin()
                            && SensorDeviceInfo.mDeviceEntity!=null
                            && ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName);

                    BleLog.dReconnect("蓝牙是否可用:"+BleUtils.getInstance().isBleEnabled()
                            +",sib是否连接状态:"+isSibBluetoothConnected()
                            +",是否主动断开:"+SensorDeviceInfo.isAcDis
                            +",设备是否正常:"+SensorDeviceInfo.sensorIsEdOrEp
                            +",是否终止发送数据:"+SensorDeviceInfo.isRightNowStopData
                            +",是否登录:"+UserInfoUtils.isLogin()
                            +",设备entity是否为空:"+(SensorDeviceInfo.mDeviceEntity!=null)
                            +",设备名称是否为空:"+(ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName))
                    );
                    if (isReConnect) {
                        BleLog.dReconnect("触发重连,当前重连时间:"+TimeUtils.millis2String(System.currentTimeMillis()), true);
                        connectBluetooth();
                        if (ScreenUtils.isScreenLock()) {
                            BleLog.dApp("当前为锁屏状态,下次重试,延迟:"+(mReconnectMillsLock/1000)+"秒");
                            mReconnectMills = mReconnectMillsLock;
                        } else {
                            BleLog.dApp("当前未锁屏,下次重试,延迟:"+(mReconnectMillsUnlock/1000)+"秒");
                            mReconnectMills = mReconnectMillsUnlock;
                        }

                        DebugFileUtils.setAppendFile("触发重连" + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----重连间隔：" + mReconnectMills);
                    } else {
                        BleLog.dReconnect("不满足条件,不执行重连", true);
                    }
                    break;
            }
        }
    };



//    private void disConnectReConnect() {
//        Log.d("蓝牙", "发起重连");
//        connectBluetooth();
//        if (ScreenUtils.isScreenLock()) {
//            mReconnectMills += 5 * 60000;
//        } else {
//            mReconnectMills = 1500L;
//        }
//        DebugFileUtils.setAppendFile("触发重连" + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----重连间隔：" + mReconnectMills);
//    }
//
//
//    protected void sendReconnectionMessage() {
//        Log.d("蓝牙:jar包回调", "不是主动断开连接,传感器正常,用户已登录,设备不为空,不立刻中止发送数据,发送handle消息,准备重连");
//        mReconnectionHandler.sendEmptyMessageDelayed(MSG_RECONNECT, mReconnectMills);
//    }

}
