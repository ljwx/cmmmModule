package com.sisensing.common.ble_new.status

object SensorGlucoseStatus {

    //1.正常 2.半小时的电流异常 3.温度过高 4.温度过低 5.血糖过高 6.血糖过低

    const val ALARM_NORMAL = 1
    const val ALARM_HALF_HOUR_CURRENT_EXCEPTION = 2
    const val ALARM_TEMPERATURE_HIGH = 3
    const val ALARM_TEMPERATURE_LOW = 4
    const val ALARM_GLUCOSE_HIGH = 5
    const val ALARM_GLUCOSE_LOW = 6

    //温度报警 0.正常  1.过低  2.过高
    const val TEMPERATURE_LOW = 1
    const val TEMPERATURE_HIGH = 2

    //电流报警 0.正常  1.过低  2.过高
    const val CURRENT_LOW = 1
    const val CURRENT_HIGH = 2

    //血糖报警  0.正常  1.过低  2.过高
    const val GLUCOSE_LOW = 1
    const val GLUCOSE_HIGH = 2

}