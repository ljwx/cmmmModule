package com.ljwx.baseswitchenv

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Vibrator
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.*
import kotlin.math.abs

class ShakeSelectAppEnv(
    private val activity: Activity,
    private val selected: (item: AppEnvItem) -> Unit,
) : SensorEventListener, DefaultLifecycleObserver {

    //传感器
    private val mSensorManager by lazy {
        activity.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    }

    //震动
    private val mVibrator by lazy {
        activity.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    //弹窗
    private val mSelectDialog by lazy {
        var choice = 0
        AlertDialog.Builder(activity)
            .setTitle("环境列表")
            .setSingleChoiceItems(AppEnvConfig.getStringArray(), 0) { dialog, i ->
                choice = i
            }.setNegativeButton("取消") { dialog, i ->
                dialog.dismiss()
            }
            .setPositiveButton("确定") { dialogInterface, i ->
                val item = AppEnvConfig.getEnvList()[choice]
                selected.invoke(item)
                dialogInterface.dismiss()
            }.create()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {//加速度变更事件
            //value[0]:X轴,value[1]:Y轴，values[2]:Z轴
            val values = event.values
            if ((abs(values[0]) > 20) || abs(values[1]) > 20 || abs(values[2]) > 20) {
                showDialog()
                mVibrator?.vibrate(200);
            }
        }
    }

    private fun showDialog() {
        if (mSelectDialog?.isShowing == false) {
            mSelectDialog?.show()
            mSelectDialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mSensorManager?.unregisterListener(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mSensorManager?.registerListener(
            this,
            mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

    }

    interface EnvCallback {
        fun selected(item: AppEnvItem)
    }

}