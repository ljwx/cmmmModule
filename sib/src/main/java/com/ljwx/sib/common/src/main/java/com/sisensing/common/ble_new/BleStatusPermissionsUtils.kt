package com.sisensing.common.ble_new

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.sisensing.common.ble.BlePermissionsUtils
import com.sisensing.common.ble.BleUtils
import com.sisensing.common.utils.Log

object BleStatusPermissionsUtils {


    private val TAG = "蓝牙,打开,权限"

    /**
     * 有连接权限
     */
    fun connectGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isEnable(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            return true
        }
        return false
    }

    fun requestPermissions() {

    }

    @SuppressLint("MissingPermission")
    fun openBle(): Boolean {
        try {
            if (!isEnable()) {
                if (PermissionUtils.isGranted(*BlePermissionsUtils.getBlePermissions())) {
                    Log.d(TAG, "有蓝牙权限,正在开启")
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    ActivityUtils.getTopActivity().startActivityForResult(enableBtIntent, 111)
                    BleUtils.getInstance().openBle()
                    return true
                } else {
                    Log.d(TAG, "没有蓝牙权限,无法开启")
                }
            } else {
                Log.d(TAG, "蓝牙已打开")
                return true
            }
        } catch (e: Exception) {
            Log.d(TAG, "开启蓝牙报错:$e")
        }
        return false
    }

}