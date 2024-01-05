package com.ljwx.basemodule.ble;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ljwx.basedialog.common.BaseDialogBuilder;
import com.sisensing.base.BaseViewModel;
import com.sisensing.common.R;
import com.sisensing.common.base.BaseActivity;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.BlePermissionsUtils;
import com.sisensing.common.ble.dialog.SimpleTipsDialog;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.ble_new.BleLibUtils;
import com.sisensing.common.ble_new.ConstBleDeviceStatus;
import com.sisensing.common.ble_new.global.BleDeviceGlobalObserver;
import com.sisensing.common.ble_new.global.BleDeviceStatusGlobal;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceRepository;
import com.sisensing.common.entity.alarm.GlobalLiveData;
import com.sisensing.common.router.RouterActivityPath;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.BroadcastManager;
import com.sisensing.common.utils.LocationUtils;
import com.sisensing.common.utils.Log;
import com.tbruyelle.rxpermissions3.RxPermissions;

import io.reactivex.rxjava3.functions.Consumer;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/7/22 15:52
 * Description:
 */
public abstract class BaseCgmConnectActivityV4<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<V, VM> {

    public boolean mBindNewDevice;

    private ActivityResultLauncher locationLauncher = LocationUtils.registerForLocationResult(this);

    //连接失败次数
//    private int connectFailCount = 0;

    private String mLinkCode;
    private String mUserId;

    //是否查找过设备
    private boolean mQueriedDevice = false;
    //是否需要连接之前的设备
    private boolean mNeedConnectOldDevice = true;

    private int permissionsCode = 777;

    private Dialog bleDialog;

    private RxPermissions rxPermissions;

    @Override
    public void init() {
        super.init();
//        LocalBleManager.getInstance().addConnectListener(this);
        BleDeviceStatusGlobal.connectStatusChange().observe(this, new BleDeviceGlobalObserver() {
            @Override
            public void onChangedCustom(Integer status, Boolean showToast) {
                if (status == ConstBleDeviceStatus.DEVICE_CONNECTED) {
                    showSuccess(getString(R.string.common_ble_connected));
                    jumpMainActivity();
                } else if (status == ConstBleDeviceStatus.DEVICE_CONNECTING) {
                    showLoading(getString(R.string.common_ble_connecting));
                } else if (status == ConstBleDeviceStatus.DEVICE_DISCONNECT) {
                    disConnected(showToast);
                } else if (status == ConstBleDeviceStatus.DEVICE_AREA_INVALID) {
                    dismissDialog();
                    SimpleTipsDialog.showAreaTips(null, false, "");
                }
            }
        });
        mUserId = UserInfoUtils.getUserId();

        //不管设备有没有连接先断开
        BleLibUtils.stopConnect();
    }

//    @Override
//    public void connected() {
//        showSuccess(getString(R.string.common_ble_connected));
//        jumpMainActivity();
//    }
//
//    @Override
//    public void connecting() {
//        showLoading(getString(R.string.common_ble_connecting));
//    }
//
//    @Override
//    public void disConnected(boolean showToast) {
//        if (ObjectUtils.isEmpty(mLinkCode)) return;
//        if (showToast) {
//            ToastUtils.showLong(R.string.common_ble_connect_fail);
//        }
//        dismissDialog();
//
//    }


//    private void jumpConnectFailPage() {
//        connectFailCount = 0;
//        ARouter.getInstance().build(RouterActivityPath.PersonalCenter.PAGE_SENSOR_CONNECT_FAIL).navigation();
//    }


    private void jumpMainActivity() {
        mNeedConnectOldDevice = false;
        ActivityUtils.finishAllActivitiesExceptNewest();
        ARouter.getInstance().build(RouterActivityPath.Launcher.PAGE_MAIN).withTransition(android.R.anim.fade_in, android.R.anim.fade_out).navigation();
        finish();
    }

    /**
     * 开始连接
     *
     * @param linkCode
     * @param
     */
    public void startConnect(String linkCode) {
        mQueriedDevice = false;
        mLinkCode = linkCode;
        if (!LocationUtils.isOpenLocation(this) && LocationUtils.useLocation(true)) {
            BleLog.dInterrupt("定位未打开,引导用户打开定位:" + linkCode);
            LocationUtils.openLocationInfo(this, locationLauncher);
            GlobalLiveData.INSTANCE.getScanCodeFail().postValue(true);
            return;
        }
        if (mBindNewDevice && LocalBleManager.getInstance().isSibBluetoothConnected()) {
            //说明之前连接过设备了
            BleLibUtils.stopConnect();
            BleLog.dInterrupt("之前连过的设备,直接停止连接:" + linkCode);
        } else {
            queryDevice(mLinkCode, mUserId);
        }

    }

    private void queryDevice(String linkCode, String userId) {

        mQueriedDevice = true;
        DeviceRepository.getInstance().queryDeviceByLinkCodeAndUserId(linkCode, userId, new RoomResponseListener<DeviceEntity>() {
            @Override
            public void response(@Nullable DeviceEntity deviceEntity) {
                if (deviceEntity == null) {
                    BleLog.dApp("数据库查不到,没连过:" + linkCode);
                    deviceEntity = new DeviceEntity();
                    deviceEntity.setUserId(userId);
                }
                deviceEntity.setBlueToothNum(linkCode);

                int deviceStatus = deviceEntity.getDeviceStatus();


                if (deviceStatus == 4) {
                    BleLog.dInterrupt("之前连过,但设备异常");
                    ToastUtils.showShort(getString(R.string.device_is_unable_to_use));
                    return;
                }

                //当连接的设备记录是过期时，返回首页，提示佩戴页
                if (deviceStatus == 2) {
                    SensorDeviceInfo.sensorIsEdOrEp = true;
                    jumpMainActivity();
                    return;
                }

                beginScan(deviceEntity);
            }
        });

    }


    private void beginScan(DeviceEntity deviceEntity) {
        BleLog.dApp("开始扫描连接:" + deviceEntity.getBlueToothNum());
        LocalBleManager.getInstance().startConnect(this, deviceEntity, true);
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            Log.d("权限", "蓝牙权限同意");
            if (!BleUtils.getInstance().isBleEnabled()) {
                BleUtils.getInstance().openBle();
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                    // 蓝牙已禁用，可以在此处启用蓝牙
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 111);
                }
            }
        } else {
            Log.d("权限", "蓝牙权限申请");
            try {
                BleUtils.getInstance().openBle();
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                    // 蓝牙已禁用，可以在此处启用蓝牙
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 111);
                }
            }catch (Exception e){

            }
        }
//        requestPermission();
    }

    // 检查并请求蓝牙权限
    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, permissionsCode);
        } else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
//        }
//        try {
//            if (!BleUtils.getInstance().isBleEnabled()) {
//                Log.d("相机权限", "蓝牙未开启");
//                BleUtils.getInstance().openBle();
//            }
//        } catch (Exception e) {
//            Log.d("相机权限", e.toString());
//        }
//        handlePermission();
    }

    @Override
    protected void onDestroy() {
//        LocalBleManager.getInstance().removeConnectListener(this);
        if (mNeedConnectOldDevice) {
            BroadcastManager.getInstance(this).sendBroadcast(Constant.BROADCAST_CONNECT_BEFORE_DEVICE);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == 0) {
//            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
//                // 蓝牙已禁用，可以在此处启用蓝牙
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, 111);
//            }
//        } else {
//            showBleDialog();
//        }
    }

    private void handlePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            Log.d("权限", "蓝牙权限同意");
            if (!BleUtils.getInstance().isBleEnabled()) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                    // 蓝牙已禁用，可以在此处启用蓝牙
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 111);
                }
            }
            if (bleDialog != null) {
                bleDialog.dismiss();
            }
        } else {
            Log.d("权限", "蓝牙权限申请");
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                    // 蓝牙已禁用，可以在此处启用蓝牙
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 111);
                }
            } catch (Exception e){

            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, permissionsCode);
//            showBleDialog();
        }
    }

    private void showBleDialog() {
        if (bleDialog == null) {
            bleDialog = new BaseDialogBuilder()
                    .setTitle(getString(R.string.tips))
                    .setContent("请开启蓝牙权限")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .showDialog(this);
        } else {
            bleDialog.show();
        }
    }

    private void requestPermission() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.request(BlePermissionsUtils.getBlePermissions()).subscribe(aBoolean -> {
            if (aBoolean) {
                try {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                        // 蓝牙已禁用，可以在此处启用蓝牙
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 111);
                    }
                } catch (Exception e){

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

    protected void disConnected(boolean showToast) {
        if (ObjectUtils.isEmpty(mLinkCode)) return;
        if (showToast) {
            ToastUtils.showLong(R.string.common_ble_connect_fail);
        }
        dismissDialog();
    }

}
