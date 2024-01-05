package com.sisensing.common.utils

fun String?.isDigit(): Boolean {
    return this?.matches("-?\\d+(\\.\\d+)?".toRegex()) == true
}

fun String?.floatString2Int():Int{
    return this?.toFloat()?.toInt() ?: 0
}