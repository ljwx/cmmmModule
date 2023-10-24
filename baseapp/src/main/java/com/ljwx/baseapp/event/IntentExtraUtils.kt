package com.ljwx.baseapp.event

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

object IntentExtraUtils {

    fun putData(mBundle: Bundle?, key: String, value: Any?) {
        when (value) {
            is Int -> mBundle?.putInt(key, value)
            is IntArray -> mBundle?.putIntArray(key, value)
            is Long -> mBundle?.putLong(key, value)
            is LongArray -> mBundle?.putLongArray(key, value)
            is String -> mBundle?.putString(key, value)
            is Boolean -> mBundle?.putBoolean(key, value)
            is Float -> mBundle?.putFloat(key, value)
            is FloatArray -> mBundle?.putFloatArray(key, value)
            is Double -> mBundle?.putDouble(key, value)
            is DoubleArray -> mBundle?.putDoubleArray(key, value)
            is Bundle -> mBundle?.putBundle(key, value)
            is Parcelable -> mBundle?.putParcelable(key, value)
            is Serializable -> mBundle?.putSerializable(key, value)
        }
    }

}