package com.sisensing.common.entity.Device

data class RemoteGlucoseBean(
    var ast: Int?, //报警状态 1：正常 2：传感器异常 3：温度过高 4：温度过低 5：血糖过高 6：血糖过低
    var i: Int?, //索引值
    var s: Int?, //变化趋势 0:平稳 1:缓慢上升 -1:缓慢下降 2:较快上升 -2:较快下降
    var t: String?, //时间戳
    var v: Float?, //血糖值(默认mmol/L)
    var userId: String?,
    var deviceId: String?,
)
