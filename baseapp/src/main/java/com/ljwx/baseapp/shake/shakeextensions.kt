package com.ljwx.baseapp.shake

import android.content.Context
import android.hardware.SensorManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.registerShake(open: Boolean = true, callback: () -> Unit) {
    if (!open) {
        return
    }
    val sensor = getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    val shakeListener = CustomShakeListener(sensor, callback)
    lifecycle.addObserver(shakeListener)
}

fun Fragment.registerShake(open: Boolean = true, callback: () -> Unit) {
    if (!open) {
        return
    }
    if (isAdded && context != null) {
        val sensor = context!!.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val shakeListener = CustomShakeListener(sensor, callback)
        lifecycle.addObserver(shakeListener)
    }
}