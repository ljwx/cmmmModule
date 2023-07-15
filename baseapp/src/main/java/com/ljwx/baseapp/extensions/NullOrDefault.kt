package com.ljwx.baseapp.extensions

inline fun String?.orDefault(default: String = ""): String = this ?: default

inline fun Int?.orDefault(default: Int = 0): Int = this ?: default

inline fun Float?.orDefault(default: Float = 0.0f): Float = this ?: default

inline fun Int?.isNullOrZero(): Boolean = this == null || this == 0