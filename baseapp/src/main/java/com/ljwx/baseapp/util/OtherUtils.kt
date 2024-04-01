package com.ljwx.baseapp.util

object OtherUtils {

    private var isDevicePad = false

    fun isDevicePad(): Boolean {
        return isDevicePad
    }

    fun setDevicePad(isPad: Boolean) {
        isDevicePad = isPad
    }

}