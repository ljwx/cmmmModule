package com.ljwx.baseapp.extensions

import com.ljwx.baseapp.regex.CommonRegex
import java.util.regex.Pattern

fun CharSequence?.isMatch(regex: String): Boolean {
    return !this.isNullOrEmpty() && Pattern.matches(regex, this)
}

inline fun CharSequence?.notNullOrBlank(): Boolean {
    return !isNullOrBlank()
}

fun CharSequence?.isEmail(): Boolean {
    return this.isMatch(CommonRegex.email)
}

fun CharSequence?.isNumber(): Boolean {
    return this.isMatch(CommonRegex.number)
}

fun CharSequence?.isDecimal(): Boolean {
    return this.isMatch(CommonRegex.decimal)
}

fun String?.toIntSafe(default: Int): Int {
    return if (isNumber()) {
        this!!.toInt()
    } else {
        default
    }
}

fun String?.toLongSafe(default: Long): Long {
    return if (isNumber()) {
        this!!.toLong()
    } else {
        default
    }
}