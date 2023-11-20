package com.ljwx.baseactivity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

open class BaseToolsActivity : AppCompatActivity() {

    private var sensorManager: SensorManager? = null
    private var sensorEventListener: SensorEventListener? = null

    open fun registerSensorShake(callback: () -> Unit) {
        val sensor = sensorManager ?: getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        sensorEventListener = sensorEventListener ?: object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {//加速度变更事件
                    //value[0]:X轴,value[1]:Y轴，values[2]:Z轴
                    val values = event.values
                    if ((abs(values[0]) > 20) || abs(values[1]) > 20 || abs(values[2]) > 20) {
                        callback.invoke()
                    }
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

        }
        sensor?.registerListener(
            sensorEventListener,
            sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(sensorEventListener)
    }

}