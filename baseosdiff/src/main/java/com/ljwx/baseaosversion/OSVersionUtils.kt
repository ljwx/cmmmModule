package com.ljwx.baseaosversion

import android.os.Build

object OSVersionUtils {

    /**
     * Platform version,  API Level,  Version code
     * Android 5.0          21          Lollipop
     * Android 5.1          22          L_MR1
     * Android 6.0          23          Marshmallow
     * Android 7.0          24          Nougat
     * Android 7.1          25          Nougat
     * Android 8.0          26          Oreo
     * Android 8.1          27          Oreo
     * Android 9.0          28          Pie
     * Android10.0          29          Quince Tart
     * Android11.0          30          Red velvet cake
     * Android12.0          31          Snow cone
     * Android 12L          32          Snow cone v2
     * Android  13          33          Tiramisu
     */

    fun greaterOrEqual13(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun greaterThan13(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun greaterOrEqual12(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    fun greaterThan12(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }

    fun greaterOrEqual11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun greaterThan11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun greaterOrEqual10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun greaterThan10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun greaterOrEqual8(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun greaterThan8(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun greaterOrEqual6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun greaterThan6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

}