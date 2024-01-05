package com.sisensing.common.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ThreadUtils
import com.sisensing.common.utils.Log
import com.tbruyelle.rxpermissions3.RxPermissions
import java.io.IOException
import java.util.UUID


object BleConnectUtils {

    private val scanResult = HashSet<String>()

    fun startDiscovery(context: FragmentActivity) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            BleLog.dTest("设备不支持蓝牙")
        } else {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //扫描结果
                receiver(context, bluetoothAdapter)
                bluetoothAdapter.startDiscovery()
            } else {
                RxPermissions(context).request(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION
                ).subscribe()
            }
        }
    }

    fun receiver(context: Context, adapter: BluetoothAdapter) {
        // 创建BroadcastReceiver以接收蓝牙设备的搜索结果
        val receiver = object : BroadcastReceiver() {
            @SuppressLint("RestrictedApi", "MissingPermission")
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // 当找到设备时
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    val cgm = "LT2302ABAB"
                    val xiaomi = "9527的Xiaomi 12"
                    val xiaomiUUID = UUID.fromString("00001105-0000-1000-8000-00805f9b34fb")
                    val randomUUID = UUID.randomUUID()
                    val netUUID = UUID.fromString("5c8c1532-5fe4-1f0a-4f93-5b55085e10a3")
                    """
                        uuid:0000110a-0000-1000-8000-00805f9b34fb
                        uuid:00001105-0000-1000-8000-00805f9b34fb
                        uuid:00001115-0000-1000-8000-00805f9b34fb
                        uuid:00001116-0000-1000-8000-00805f9b34fb
                        uuid:0000112f-0000-1000-8000-00805f9b34fb
                        uuid:00001112-0000-1000-8000-00805f9b34fb
                        uuid:0000111f-0000-1000-8000-00805f9b34fb
                        uuid:00001132-0000-1000-8000-00805f9b34fb
                        uuid:98b97136-36a2-11ea-8467-484d7e99a198
                    """.trimIndent()
                    if (xiaomi == device?.name) {
                        adapter.cancelDiscovery()
                        BleLog.dTest("开始连接")
                        device.createBond()
                        device.uuids.forEach {
                            BleLog.dTest("uuid:" + it)
                        }
                        val device2 = adapter.getRemoteDevice(device.address)
                        val socket = device2.createRfcommSocketToServiceRecord(netUUID)
                        Thread {
                            try {
                                socket.connect()
                            } catch (e: IOException) {
                                BleLog.dTest(e.toString())
                            }
                        }.start()
                    }
                    // 处理设备
                    device?.let {
                        if (it.name !in scanResult) {
                            scanResult.add(it.name)
                            BleLog.dTest("扫描到的设备名:" + device?.name)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

}