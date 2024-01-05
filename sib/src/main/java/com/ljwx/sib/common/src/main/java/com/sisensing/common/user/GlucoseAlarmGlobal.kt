package com.sisensing.common.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.sisensing.common.base.RetrofitUtils
import com.sisensing.common.entity.alarm.UserGlucoseAlarmSettingsData
import com.sisensing.common.net.GlucoseAlarmApiService
import com.sisensing.common.entity.notification.FcmMessageBean
import com.sisensing.common.utils.GlucoseAlarmUtils
import com.sisensing.common.utils.Log
import com.sisensing.event.SingleLiveEvent
import okhttp3.MediaType
import okhttp3.RequestBody

object GlucoseAlarmGlobal {

    private val SP_KEY = "user_alarm_settings"

    private val TIME_VERY = "alarm_time_very"
    private val TIME_LOW = "alarm_time_low"
    private val TIME_HIGH = "alarm_time_high"
    private val TIME_LOSE = "alarm_time_lose"

    private val _alarmSettings = MutableLiveData<UserGlucoseAlarmSettingsData>()
    val alarmSettings: LiveData<UserGlucoseAlarmSettingsData> = _alarmSettings

    val friendAlarm = SingleLiveEvent<FcmMessageBean?>()

    private val _alarmCount = MutableLiveData<Int>()

    @JvmStatic
    val alarmCount: LiveData<Int> = _alarmCount

    private val MINUTE = 1000 * 60

    private val api by lazy {
        RetrofitUtils.getInstance().getRequest(GlucoseAlarmApiService::class.java)
    }

    init {
        Log.d("血糖报警", "全局初始化")
    }

    fun initData() {
        if (alarmSettings.value == null) {
            changeSettings(getSettings())
            Log.d("血糖报警", "修改设置")
        }
    }

    @JvmStatic
    fun changeSettings(settings: UserGlucoseAlarmSettingsData?) {
        if (settings == null) {
            return
        }
        val json = GsonUtils.toJson(settings)
        SPUtils.getInstance().put(SP_KEY, json)
        _alarmSettings.postValue(settings!!)
    }

    fun getSettings(): UserGlucoseAlarmSettingsData? {
        val json = SPUtils.getInstance().getString(SP_KEY)
        try {
            return GsonUtils.fromJson(json, UserGlucoseAlarmSettingsData::class.java)
        } catch (e: Exception) {
            return null
        }
        return null
    }

    /**
     * 获取间隔时间
     */
    fun getDuration(type: Int): Int {
        initData()
        if (GlucoseAlarmUtils.isDebug()) {
            return 2
        }
        when (type) {
            GlucoseAlarmUtils.VERY_LOW -> {
                alarmSettings.value?.veryLowAlarm?.apply {
                    if (alarmInterval != null && alarmInterval!! > 0) {
                        Log.d("血糖报警", "极低血糖间隔:$alarmInterval")
                        return alarmInterval!!
                    }
                }
            }

            GlucoseAlarmUtils.GLUCOSE_LOW -> {
                alarmSettings.value?.lowAlarm?.apply {
                    if (alarmInterval != null) {
                        Log.d("血糖报警", "低血糖间隔:$alarmInterval")
                        return alarmInterval!!
                    }
                }
            }

            GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                alarmSettings.value?.highAlarm?.apply {
                    if (alarmInterval != null) {
                        Log.d("血糖报警", "高血糖间隔:$alarmInterval")
                        return alarmInterval!!
                    }
                }
            }

            GlucoseAlarmUtils.SIGNAL_LOSS -> {
                alarmSettings.value?.signalLostAlarm?.apply {
                    if (duration != null && duration!! > 0) {
                        Log.d("血糖报警", "信号丢失间隔:$duration")
                        return duration!!
                    }
                }
            }
        }
        return 30
    }

    @JvmStatic
    fun isOverTime(type: Int): Boolean {
        if (type == GlucoseAlarmUtils.SIGNAL_LOSS) {
            if (getLastAlarmTime(type) < 1) {
                Log.d("血糖报警", "上次信号时间为空,相当于未佩戴,不报警")
                return false
            }
        }
        //五笔血糖之间可能有误差,加一个buff
        val buff = 10000
        val diff = (System.currentTimeMillis() - getLastAlarmTime(type) + buff)
        val duration = getDuration(type) * MINUTE
        Log.d("血糖报警", "当前间隔:${diff / MINUTE}-设置间隔:${duration / MINUTE}")
        return diff > duration
    }

    /**
     * 最后提醒时间
     */
    fun getLastAlarmTime(type: Int): Long {
        val userId = UserInfoUtils.getUserId()
        return when (type) {
            GlucoseAlarmUtils.VERY_LOW -> {
                SPUtils.getInstance().getLong(TIME_VERY + userId, 0)
            }

            GlucoseAlarmUtils.GLUCOSE_LOW -> {
                SPUtils.getInstance().getLong(TIME_LOW + userId, 0)
            }

            GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                SPUtils.getInstance().getLong(TIME_HIGH + userId, 0)
            }

            GlucoseAlarmUtils.SIGNAL_LOSS -> {
                SPUtils.getInstance().getLong(TIME_LOSE + userId, 0)
            }

            else -> 0
        }
    }

    /**
     * 保存提醒时间
     */
    fun saveAlarmTime(type: Int) {
        val userId = UserInfoUtils.getUserId()
        when (type) {
            GlucoseAlarmUtils.VERY_LOW -> {
                Log.d("血糖报警", "存值:" + System.currentTimeMillis())
                SPUtils.getInstance().put(TIME_VERY + userId, System.currentTimeMillis())
            }

            GlucoseAlarmUtils.GLUCOSE_LOW -> {
                SPUtils.getInstance().put(TIME_LOW + userId, System.currentTimeMillis())
            }

            GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                SPUtils.getInstance().put(TIME_HIGH + userId, System.currentTimeMillis())
            }

            GlucoseAlarmUtils.SIGNAL_LOSS -> {
                SPUtils.getInstance().put(TIME_LOSE + userId, System.currentTimeMillis())
            }
        }
    }

    /**
     * 阈值
     */
    @JvmStatic
    fun getThreshold(type: Int): Int? {
        initData()
        when (type) {
            GlucoseAlarmUtils.VERY_LOW -> {
                alarmSettings.value?.veryLowAlarm?.apply {
                    if (enable == true && threshold != null) {
                        Log.d("血糖报警", "极低血糖阈值:$threshold")
                        return threshold!!.toInt()
                    }
                }
                return null
            }

            GlucoseAlarmUtils.GLUCOSE_LOW -> {
                alarmSettings.value?.lowAlarm?.apply {
                    if (enable == true && threshold != null) {
                        Log.d("血糖报警", "低血糖阈值:$threshold")
                        return threshold!!.toInt()
                    }
                }
                return null
            }

            GlucoseAlarmUtils.GLUCOSE_HIGH -> {
                alarmSettings.value?.highAlarm?.apply {
                    if (enable == true && threshold != null) {
                        Log.d("血糖报警", "高血糖阈值:$threshold")
                        return threshold!!.toInt()
                    }
                }
                return null
            }

            GlucoseAlarmUtils.SIGNAL_LOSS -> {
                alarmSettings.value?.signalLostAlarm?.apply {
                    if (enable == true && duration != null) {
                        return duration!!
                    }
                }
                return null
            }
        }
        return null
    }

    fun pushServerKnow(type: Int?, flowId: String? = null) {
        val map = HashMap<String, Any?>()
        map["eventType"] = type
        map["followId"] = flowId
        val requestBody =
            RequestBody.create(MediaType.parse("application/json"), GsonUtils.toJson(map))
        api.updateAlarmKnow(requestBody).subscribe()
    }

    fun addAlarmCount(type: Int) {
        _alarmCount.postValue((_alarmCount.value ?: 0) + 1)
    }

    fun clearCount() {
        _alarmCount.postValue(0)
    }

}