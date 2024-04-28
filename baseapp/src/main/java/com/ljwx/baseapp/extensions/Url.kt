package com.ljwx.baseapp.extensions

import android.util.Log
import com.ljwx.baseapp.regex.CommonRegexJ
import com.ljwx.baseapp.util.BaseModuleLog
import java.util.regex.Pattern

fun String?.isUrlFull(): Boolean {
    return this?.isMatch(CommonRegexJ.URL_FULL) == true
}

fun String?.isHostMatch(host: String): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val pattern = Pattern.compile(CommonRegexJ.URL_FULL_HOST, Pattern.CASE_INSENSITIVE)
    val mather = pattern.matcher(this)
    if (mather.find()) {
        val getHost = mather.group()
        BaseModuleLog.d("提取的域名", getHost)
        return host.equals(getHost)
    }
    return false
}

fun String?.isFullUrlFromHost(host: String): Boolean {
    return isUrlFull() && isHostMatch(host)
}