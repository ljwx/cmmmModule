package com.ljwx.baseaosversion

object OSDiffBluetoothUtils {

    /**
     * Android 11 及以下目标版本
     *
     * 1.BLUETOOTH：访问蓝牙适配器的权限，用于执行蓝牙操作。
     *
     * 2.BLUETOOTH_ADMIN：管理蓝牙适配器的权限，包括启用/禁用蓝牙、扫描设备和进行配对等操作。
     *
     * 3.ACCESS_FINE_LOCATION 或 ACCESS_COARSE_LOCATION：访问设备位置的权限。在 Android 6.0 及以上版本中，需要获取位置权限才能扫描附近的蓝牙设备。
     *
     * 4.ACCESS_BACKGROUND_LOCATION：在后台访问设备位置的权限。该权限通常在后台扫描蓝牙设备时使用。
     *
     *
     * Android 12 中的新蓝牙权限
     * 如果您的应用查找蓝牙设备（如蓝牙低功耗 (BLE) 外围设备），请向应用的清单中添加 BLUETOOTH_SCAN 权限。
     * 如果您的应用使当前设备可被其他蓝牙设备检测到，请向应用的清单中添加 BLUETOOTH_ADVERTISE 权限。
     * 如果您的应用与已配对的蓝牙设备通信，请向应用的清单中添加 BLUETOOTH_CONNECT 权限。
     * 对于旧版蓝牙相关的权限声明，请将 android:maxSdkVersion 设为 30。此应用兼容性步骤有助于系统仅向您的应用授予在搭载 Android 12 的设备上安装时所需的蓝牙权限。
     *
     */

    fun permissionGer() {

    }

}