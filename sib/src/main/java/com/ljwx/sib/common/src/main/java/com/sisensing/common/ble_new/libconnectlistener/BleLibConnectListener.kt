package com.sisensing.common.ble_new.libconnectlistener

import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.sisensing.common.R
import com.sisensing.common.ble.BleLog
import com.sisensing.common.ble.BleUtils
import com.sisensing.common.ble.dialog.SimpleTipsDialog
import com.sisensing.common.ble.v4.GlucoseThreadExecutor
import com.sisensing.common.ble.v4.data.GlucoseDataInfo
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.ble_new.BleDeviceGlucoseDataUtils
import com.sisensing.common.ble_new.BleDeviceAlgorithmUtils
import com.sisensing.common.ble_new.BleDeviceConnectUtils
import com.sisensing.common.ble_new.BleLibUtils
import com.sisensing.common.ble_new.ConstBleDeviceStatus
import com.sisensing.common.ble_new.global.BleDeviceStatusGlobal
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.database.RoomTask
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import com.sisensing.common.entity.alarm.GlobalLiveData
import com.sisensing.common.share.LogUploadModel
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.BroadcastManager
import com.sisensing.common.utils.LocationUtils
import com.sisensing.common.utils.Log
import no.sisense.android.api.Constant

open class BleLibConnectListener(val context: Context) : BleLibUnusedConnectListener() {
    override fun <T : Any?> onGJDataRecive(p0: BluetoothDevice?, t: T) {
        BleDeviceGlucoseDataUtils.bleLibDataReceive(t)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        //重启服务
        ThreadUtils.runOnUiThread{
            BleLog.eLib("蓝牙库服务被销毁,重新初始化蓝牙库服务");
            BleDeviceConnectUtils.changeConnectStatus(no.sisense.android.api.Constant.STATE_DISCONNECTED)
            BleLibUtils.init(context, true)
            BleDeviceConnectUtils.disconnectAndReconnect()
            LogUploadModel.getInstance().uploadConnectInfo("Ble service disconnected:" + SensorDeviceInfo.mDeviceName);
        };
    }

    override fun onConnectionSate(bluetoothDevice: BluetoothDevice?, s: String, status: Int) {
        BleDeviceConnectUtils.changeConnectStatus(status)
        BleLog.eLib("连接状态回调," + s + "：" + BleLog.status(status))
        when (status) {
            Constant.STATE_CONNECTED -> {
                GlucoseDataInfo.mSyncTimeMill = System.currentTimeMillis()
                UserInfoUtils.isDeviceConnected = true
                SensorDeviceInfo.isAcDis = false
                SensorDeviceInfo.mBluetoothDevice = bluetoothDevice
                BleDeviceConnectUtils.stopReconnect()
            }

            Constant.STATE_DISCONNECTED -> {
                ThreadUtils.runOnUiThread {
                    BleDeviceConnectUtils.disconnectAndReconnect()
                }
                GlobalLiveData.scanCodeFail.postValue(true)
            }

            Constant.STATE_CONNECTING, Constant.STATE_SCANNING -> {
                if (BleUtils.getInstance().isBleEnabled) {
                    if (LocationUtils.isOpenLocation(Utils.getApp()) || !LocationUtils.useLocation(
                            true
                        )
                    ) {
                        SensorDeviceInfo.isAcDis = false
                        BleDeviceStatusGlobal.changeConnectStatus(ConstBleDeviceStatus.DEVICE_CONNECTING)
                    } else {
                        BleLog.dLib("onConnectionSate:定位未打开,主动停止连接,发送定位未打开广播")
                        BleLibUtils.stopConnect()
                        BroadcastManager.getInstance(Utils.getApp())
                            .sendBroadcast(com.sisensing.common.constants.Constant.BROADCAST_BLUETOOTH_OPEN_LOCATION_NOT_OPEN)
                    }
                }
            }

            Constant.STATE_AUTHENTICATION_FAIL -> {
                SimpleTipsDialog.showAuthFail()
                ThreadUtils.runOnUiThread { BleDeviceConnectUtils.disconnectAndReconnect() }
                GlobalLiveData.scanCodeFail.postValue(true)
            }

            else -> {}
        }
    }

    override fun readCharacteristicWithBattery(
        bluetoothDevice: BluetoothDevice,
        s: String,
        s1: String?,
        s2: String?,
        s3: String?,
        s4: String?,
        s5: String?,
        s6: String?
    ) {
        val characteristicMap = HashMap<String, String>()
        var bleProtocolVersion: String? = null
        if (!TextUtils.isEmpty(s1)) {
            //设备制造商
            characteristicMap.put("manufacturer", s1!!)
            BleLog.dLib("设备制造商:" + s1);
        }
        if (!TextUtils.isEmpty(s2)) {
            //型号，系列
            characteristicMap.put("model", s2!!)
        }
        if (!TextUtils.isEmpty(s3)) {
            //序列号
            characteristicMap.put("serial", s3!!);
        }
        if (!TextUtils.isEmpty(s4)) {
            //硬件版本号
            characteristicMap.put("hardware", s4!!);
        }
        if (!TextUtils.isEmpty(s5)) {
            //固件版本号
            characteristicMap.put("firmware", s5!!)
        }
        if (!TextUtils.isEmpty(s6)) {
            //软件版本号
            characteristicMap.put("software", s6!!)
            if (s6!!.contains("_")) {
                bleProtocolVersion = s6.split("_")[1];
            }
        }
        LogUtils.d("蓝牙协议", s + "," + bleProtocolVersion);
        //预先保存这些信息,实际连接不一定成功
        //如果当前设备名中包含有当前连接码的前四位才给赋值
        if (SensorDeviceInfo.mDeviceEntity != null && ObjectUtils.isNotEmpty(s) && ObjectUtils.isNotEmpty(
                SensorDeviceInfo.mDeviceEntity.getBlueToothNum()
            )
            && s.contains(SensorDeviceInfo.mDeviceEntity.getBlueToothNum().substring(0, 4))
        ) {
            SensorDeviceInfo.mDeviceName = s;
            SensorDeviceInfo.mDeviceEntity.setDeviceName(SensorDeviceInfo.mDeviceName);
            SensorDeviceInfo.mMacAddress = bluetoothDevice.getAddress();
            SensorDeviceInfo.mDeviceEntity.setMacAddress(SensorDeviceInfo.mMacAddress);
            val algorithmVersion = BleDeviceAlgorithmUtils.getCurrentAlgorithm().algorithmVersion
            SensorDeviceInfo.mDeviceEntity.setConnectMill(System.currentTimeMillis());
            SensorDeviceInfo.mDeviceEntity.setAlgorithmVersion(algorithmVersion);
            val characteristic = GsonUtils.toJson(characteristicMap);
            SensorDeviceInfo.mDeviceEntity.characteristic = characteristic;
        }
    }

    override fun <T : Any?> CGMSystemMessage(bluetoothDevice: BluetoothDevice, p1: Int, p2: T) {
        BleLog.dLib("接收到发射板系统信息," + getDeviceName(bluetoothDevice));
    }

    override fun isCanReadSugar(bluetoothDevice: BluetoothDevice) {
        BleLog.dLib("一切准备就绪,isCanReadSugar回调")
        if (!BleLibUtils.areaCodeIsValid(bluetoothDevice)) {
            BleDeviceStatusGlobal.changeConnectStatus(
                ConstBleDeviceStatus.DEVICE_AREA_INVALID,
                false
            )
            BleDeviceConnectUtils.disconnectAndReconnect()
            return
        }
        BleDeviceConnectUtils.deviceConnectSuccess()
        //创建线程池
        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor(
            "onServieDiscovered线程池中还有任务在执行。。。" + System.currentTimeMillis(),
            "onServieDiscovered线程池中任务执行完毕。。。" + System.currentTimeMillis()
        )
        //从数据库获取最后一笔血糖数据
        RoomTask.singleTask(
            AppDatabase.getInstance().bloodEntityDao.getLastBloodGlucose(
                UserInfoUtils.getUserId(),
                SensorDeviceInfo.mDeviceName
            )
        ) { bloodGlucoseEntity: BloodGlucoseEntity? ->
            //本地存储的血糖数据最后一个索引值
            var lastIndex = 0
            if (ObjectUtils.isNotEmpty(bloodGlucoseEntity)) {
                lastIndex = bloodGlucoseEntity!!.index
                Log.d("蓝牙:jar包回调", "从数据库查询最后一笔血糖index:$lastIndex")
                GlucoseDataInfo.mProcessedTimeMill = bloodGlucoseEntity!!.processedTimeMill
            }
            GlucoseDataInfo.mCurrentInsertIndex = lastIndex
            LogUploadModel.getInstance()
                .uploadConnectInfo("-----------------------本地存储的血糖对应index=" + GlucoseDataInfo.mCurrentInsertIndex)
            BleLibUtils.getNextIndexGlucose(bluetoothDevice, ++lastIndex)
        }
    }

    override fun scanError(i: Int) {
        BleLog.dLib("scanError:" + i);
        LogUploadModel.getInstance().uploadConnectInfo("Ble Scan Error: " + i);
        ThreadUtils.runOnUiThread {
            ToastUtils.showShort(R.string.personalcenter_sensor_connect_fail)
        }
    }
}