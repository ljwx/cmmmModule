package com.sisensing.common.utils

import com.sisensing.common.constants.DailyTrendConst

fun millisInDays(millis: Long, days: Float = 1f): Boolean {
    return millis + (DailyTrendConst.ONE_DAY_MILLIS * days) > System.currentTimeMillis()
}