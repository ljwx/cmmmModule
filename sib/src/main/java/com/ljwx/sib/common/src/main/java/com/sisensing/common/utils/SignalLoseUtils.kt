package com.sisensing.common.utils

import com.ljwx.baseapp.callback.CallbackType
import com.sisensing.common.user.GlucoseAlarmGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SignalLoseUtils {

    private val interval = 1000L * 290

    private var job: Job? = null

    private var callback: CallbackType? = null

    private fun isEnable(): Boolean {
        return GlucoseAlarmGlobal.getThreshold(GlucoseAlarmUtils.SIGNAL_LOSS) != null
    }

    fun enableChange(enable: Boolean) {
        if (enable) {
            if (GlucoseAlarmGlobal.isOverTime(GlucoseAlarmUtils.SIGNAL_LOSS)) {
                callback?.invoke(1)
            }
        }
    }

    @JvmStatic
    fun updateSignal() {
        Log.d("血糖报警", "更新信号时间")
        GlucoseAlarmGlobal.saveAlarmTime(GlucoseAlarmUtils.SIGNAL_LOSS)
    }

    @JvmStatic
    fun launchPoll() {
        job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                delay(if (GlucoseAlarmUtils.isDebug()) 10000 else interval)
                Log.d("血糖报警", "是否开启:"+ isEnable())
                if (isEnable() && GlucoseAlarmGlobal.isOverTime(GlucoseAlarmUtils.SIGNAL_LOSS)) {
                    Log.d("血糖报警", "开启且超过报警间隔")
                    withContext(Dispatchers.Main) {
                        callback?.invoke(1)
                    }
                }
            }
        }
    }

    @JvmStatic
    fun setCallback(callback: CallbackType) {
        this.callback = callback
    }

    @JvmStatic
    fun cancel() {
        Log.d("血糖报警", "信号丢失轮询取消")
        job?.cancel()
    }

}