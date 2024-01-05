package com.sisensing.common.ble;

import static android.content.Context.BIND_AUTO_CREATE;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.Utils;
import com.sisensing.common.dialog.DialogPermissionExplainLocation;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.listener.CallbackType;
import com.sisensing.common.utils.LocationUtils;
import com.sisensing.common.utils.Log;
import com.sisensing.common.utils.RxPermissionUtils;
import com.tbruyelle.rxpermissions3.RxPermissions;

import io.reactivex.rxjava3.functions.Consumer;


/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.ble
 * @Author: f.deng
 * @CreateDate: 2021/3/5 15:18
 * @Description:蓝牙管理类
 */
public final class LocalBleManagerV4 implements IBle {


    //定义的中间人对象
    private LocalBleServiceProxy mBleService;
    //申请权限次数，部分手机第一次申请权限返回false，第二次返回true,需申请两次
    private int mPermissionRequestCount = 0;

    private OnServiceListener serviceListener;

    private RxPermissions mRxPermissions;

    private Dialog mDialogPermissionExplain;

    //判断是否已经初始化过
    private boolean isInit = false;

    private LocalBleManagerV4() {

    }

    private static class Inner {
        private static final LocalBleManagerV4 INSTANCE = new LocalBleManagerV4();
    }

    public static LocalBleManagerV4 getInstance() {
        return Inner.INSTANCE;
    }


    /**
     * 蓝牙管理类初始化相关操作
     *
     * @param context
     */
    public void init(Context context) {
        if(isInit && mBleService != null){
            return;
        }
        isInit = true;
        BleLog.dApp("app蓝牙service初始化");
        Intent intent = new Intent(context, LocalBleServiceProxy.class);
        context.bindService(intent, new MyLocalConnection(), BIND_AUTO_CREATE);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }*/
    }

    private class MyLocalConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBleServiceProxy.LocalBinder myBinder = (LocalBleServiceProxy.LocalBinder) service;
            mBleService = myBinder.getService();
            Log.d("蓝牙", "app蓝牙服务启动成功");
            if (serviceListener != null){
                serviceListener.serviceCreated(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("蓝牙", "app蓝牙服务 disconnected");
        }

    }


    /**
     * 蓝牙连接状态
     *
     * @param connectListener
     */
    @Override
    public void addConnectListener(CgmConnectListener connectListener) {
        if (mBleService != null && connectListener != null) {
            mBleService.addConnectListener(connectListener);
        }
    }

    @Override
    public void removeConnectListener(CgmConnectListener connectListener) {
        if (mBleService != null && connectListener != null) {
            mBleService.removeConnectListener(connectListener);
        }
    }

    /**
     * 获取服务中的蓝牙连接状态
     *
     * @return
     */
    public int getConnectStatus() {
        int status = no.sisense.android.api.Constant.STATE_DISCONNECTED;
        if (mBleService != null) {
            status = mBleService.getConnectStatus();
            BleLog.eApp("获取蓝牙当前连接状态CURRENT_CONNECT_STATUS:"+status+("=0:未连接,1:已连接,2:连接中,3:断开中,4:扫描中"));
        } else {
            BleLog.eApp("bleService为空,使用断开的默认值");
        }
        return status;
    }

    /**
     * 传感器状态
     *
     * @param statusListener
     */
    @Override
    public void setStatusListener(CgmStatusListener statusListener) {
        if (mBleService != null && statusListener != null) {
            mBleService.setStatusListener(statusListener);
        }
    }

    @Override
    public void updateDevice(DeviceEntity deviceEntity) {
        if (mBleService != null) {
            mBleService.updateDevice(deviceEntity);
        }
    }

    /**
     * 开始连接蓝牙
     *
     * @param
     */
    public void startConnect(FragmentActivity activity, final DeviceEntity entity, boolean reOpenBle) {
        Log.d("蓝牙", "有页面让我开始蓝牙连接");
        if (mBleService == null) {
            LocalBleManagerV4.getInstance().init(Utils.getApp());
            BleLog.dInterrupt("蓝牙服务为空,return不连接");
            return;
        }

        //mBleService.setReOpenBle(reOpenBle);
//        mBleService.startConnect(entity);

        mRxPermissions = RxPermissionUtils.getRxPermission(activity);

        mPermissionRequestCount = 0;
        verifyPermissionExplain(mRxPermissions, entity);
//        requestBlePermission(mRxPermissions, entity);
    }

    public void startConnect(Fragment fragment, final DeviceEntity entity, boolean reOpenBle) {
        Log.d("蓝牙", "有页面让我开始蓝牙连接");
        if (mBleService == null) {
            return;
        }

        //mBleService.setReOpenBle(reOpenBle);
//        mBleService.startConnect(entity);

        mRxPermissions =  new RxPermissions(fragment);

        mPermissionRequestCount = 0;

        verifyPermissionExplain(mRxPermissions, entity);
//        requestBlePermission(mRxPermissions, entity);
    }

    @Override
    public void startConnect(DeviceEntity deviceEntity) {
        if (BleUtils.getInstance().isBleEnabled()){
            BleLog.dApp("蓝牙可用,开始连接");
            mBleService.startConnect(deviceEntity);
        }else {
            BleLog.dInterrupt("蓝牙不可用,打开蓝牙");
            BleUtils.getInstance().openBle();
        }
    }

    /**
     * 断开连接
     */
    @Override
    public void stopConnect() {
        if (mBleService != null) {
            mBleService.stopConnect();
        }
    }


    /**
     * 判断蓝牙服务是否处于连接状态
     *
     * @return
     */
    @Override
    public boolean isSibBluetoothConnected() {
        if (ObjectUtils.isEmpty(mBleService)) {
            return false;
        }
        return mBleService.isSibBluetoothConnected();
    }

    /**
     * 重置算法中间变量
     *
     * @param deviceName
     */
    @Override
    public void releaseAlgorithmContext(String deviceName) {
        mBleService.releaseAlgorithmContext(deviceName);
    }


    /**
     * 申请蓝牙位置权限
     *
     * @param rxPermissions
     * @param entity
     */
    private void requestBlePermission(RxPermissions rxPermissions, DeviceEntity entity) {
        BleLog.dApp("请求权限");
        mPermissionRequestCount++;
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            BleLog.dApp("版本>=31,多了蓝牙扫描和连接的权限申请");
            permissions = new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        } else {
            BleLog.dApp("版本<31,申请定位权限");
            permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
        }
        rxPermissions.request(BlePermissionsUtils.getConnectPermissions()).subscribe(aBoolean -> {
            if (aBoolean) {
                BleLog.dApp("权限通过");
                LocationUtils.getLocation(Utils.getApp());
                startConnect(entity);
            } else {
                BleLog.dInterrupt("权限未通过");
                if (mPermissionRequestCount < 2) {
                    requestBlePermission(rxPermissions, entity);
                    verifyPermissionExplain(rxPermissions, entity);
//                    requestBlePermission(rxPermissions, entity);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                LogUtils.d("connect_error",throwable.getMessage());
                BleLog.eApp("权限判定出错:"+throwable.getMessage());
            }
        });
    }

    public void verifyPermissionExplain(RxPermissions rxPermissions, DeviceEntity entity) {
        if (BlePermissionsUtils.moreAndroid12()) {
            requestBlePermission(rxPermissions, entity);
        } else {
            if (PermissionUtils.isGranted(BlePermissionsUtils.getConnectPermissions())) {
                requestBlePermission(rxPermissions, entity);
                return;
            }
            if (mDialogPermissionExplain == null) {
                mDialogPermissionExplain = new DialogPermissionExplainLocation(ActivityUtils.getTopActivity(), new CallbackType() {
                    @Override
                    public void invoke(int type) {
                        requestBlePermission(rxPermissions, entity);
                    }
                });
            }
            mDialogPermissionExplain.show();
        }
    }

    public boolean isServiceRunning(){
        return mBleService != null;
    }

    public void setOnServiceListener(OnServiceListener listener){
        this.serviceListener = listener;
    }

    public interface OnServiceListener{
        void serviceCreated(boolean b);
    }
}
