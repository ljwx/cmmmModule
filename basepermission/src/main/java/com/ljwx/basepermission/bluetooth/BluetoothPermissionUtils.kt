package com.ljwx.basepermission.bluetooth

import android.Manifest
import android.os.Build

object BluetoothPermissionUtils {

    @JvmStatic
    fun getConnectPermissions(): Array<String> {
        "[10(Q):29---[11(R):30]---[12(S):31]---[13(T):33]"
        return if (moreAndroid12()) arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT
        ) else arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @JvmStatic
    fun getBackgroundPermissions(): Array<String> {
        return if (moreAndroid12()) emptyArray() else arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.FOREGROUND_SERVICE
        )
    }

    private fun moreAndroid12(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

}