package com.sisensing.common.ble_new

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.preference.PreferenceManager
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.Utils
import com.sisensing.common.ble.BleLog
import com.sisensing.common.ble.area.DeviceAreaCodeUtils
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.ble_new.libconnectlistener.BleLibConnectListener
import com.sisensing.common.constants.Constant
import no.sisense.android.api.SisenseBluetooth

object BleLibUtils {

    private var mSiBluetooth: SisenseBluetooth? = null

    private var mInitialized = false

    /**
     * 蓝牙库配置
     */
    fun setConfig(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        //1.连接码全匹配 2.过滤写死2
        preferences.edit().putInt("scantypematch", 2).apply()
    }

    fun isSkuLib(): Boolean {
        return true
    }

    /**
     * 蓝牙库初始化
     */
    @JvmOverloads
    @JvmStatic
    fun init(context: Context, force: Boolean = false) {
        if (mSiBluetooth == null) {
            mSiBluetooth = SisenseBluetooth.getInstance(context)
            mSiBluetooth?.setOnSibListener(BleLibConnectListener(context))
        }
        if (BleStatusPermissionsUtils.connectGranted(context) && (!mInitialized || force)) {
            mSiBluetooth?.init()
            setConfig(context)
            mSiBluetooth?.v120RegisterKey(
                Constant.BLE_KEY,
                Constant.BLE_KEY.length,
                Utils.getApp()
            )
            mInitialized = true
        }
    }

    @JvmStatic
    fun isInitialized(): Boolean {
        return mInitialized
    }

    @JvmStatic
    fun stopConnect() {
        BleLog.eApp("主动断开连接");
        SensorDeviceInfo.isAcDis = true;
//        mSiBluetooth.BleBluthtoothDisConnect(1);
        if (ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName)) {
            if (SensorDeviceInfo.mBluetoothDevice == null) {
                mSiBluetooth?.BleBluthtoothDisConnect(null, SensorDeviceInfo.mDeviceName);
            } else {
                mSiBluetooth?.BleBluthtoothDisConnect(
                    SensorDeviceInfo.mBluetoothDevice,
                    SensorDeviceInfo.mDeviceName
                );
            }
        }
    }

    fun connectDevice(matchStr: String) {
        mSiBluetooth?.BleBluthtoothConnectStart(matchStr, 1, 1)
    }

    fun isConnect(): Boolean {
        return mSiBluetooth?.getDeviceStatus(
            SensorDeviceInfo.mBluetoothDevice,
            SensorDeviceInfo.mDeviceName
        ) == true
    }

    @JvmStatic
    fun removeListener() {
        mSiBluetooth?.removeSibListener()
    }


    @JvmStatic
    fun areaCodeIsValid(bluetoothDevice: BluetoothDevice): Boolean {
        val manu = mSiBluetooth?.getAllCharacticOfDevice(bluetoothDevice)?.getmManufacturer()
        if (!DeviceAreaCodeUtils.isValid(manu)) {
            BleLog.dLib("不支持的设备制造商-sku:$manu")
            return false
        } else {
            return true
        }
    }

    /**
     * 从传感器获取index的下一条数据
     */
    @JvmStatic
    fun getNextIndexGlucose(bluetoothDevice: BluetoothDevice, lastIndex: Int) {
        BleLog.dLib("从传感器获取index的下一条数据")
        val time = (System.currentTimeMillis() / 1000).toInt()
        mSiBluetooth?.getDataSugarFour(bluetoothDevice, lastIndex, time, "adcd1234", 0)
    }

    fun getSDKVersionMessage(): String {
        return mSiBluetooth?.sdkVersionMessage ?: ""
    }

}