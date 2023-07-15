package com.ljwx.baseapp.extensions

import com.ljwx.baseapp.common.CommonRegex
import java.util.regex.Pattern

fun CharSequence?.isMatch(regex: String):Boolean {
    return !this.isNullOrEmpty() && Pattern.matches(regex, this)
}

inline fun CharSequence?.notNullOrBlank(): Boolean {
    return !isNullOrBlank()
}

fun CharSequence?.isEmail() :Boolean{
    return this.isMatch(CommonRegex.email)
}

fun CharSequence?.isInt() :Boolean{
    return this.isMatch(CommonRegex.integer)
}

fun CharSequence?.isFloat() :Boolean{
    return this.isMatch(CommonRegex.decimal)
}