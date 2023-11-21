package com.ljwx.baseapp.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.abs

class CustomShakeListener(
    private val sensorManager: SensorManager?,
    private val callback: () -> Unit
) : SensorEventListener, DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        sensorManager?.registerListener(
            this, sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {//加速度变更事件
            //value[0]:X轴,value[1]:Y轴，values[2]:Z轴
            val values = event.values
            if ((abs(values[0]) > 20) || abs(values[1]) > 20 || abs(values[2]) > 20) {
                callback()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        sensorManager?.unregisterListener(this)
    }

}