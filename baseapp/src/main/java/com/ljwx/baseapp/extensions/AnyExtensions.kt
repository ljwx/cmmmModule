package com.ljwx.baseapp.extensions

import androidx.annotation.StringRes
import com.blankj.utilcode.util.StringUtils

inline val Any.TAG_CLASS: String
    get() = this.javaClass.simpleName

fun Any.getStringRes(@StringRes id: Int): String {
    return StringUtils.getString(id)
}