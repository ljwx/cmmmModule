package com.sisensing.common.ble.area

object DeviceAreaCodeUtils {

    private val verifyAreaCode = "GNL"

    private fun openAreaVerify(): Boolean {
        return true
    }

    @JvmStatic
    fun getAreaCode(code: String?): String {
        if (code.isNullOrBlank()) {
            return "蓝牙库返回空值"
        }
        val split = code.split("-")
        if (split.size < 2) {
            return "蓝牙库返回没带区域码"
        }
        return split[1]
    }

    @JvmStatic
    fun isValid(code: String?): Boolean {
        if (!openAreaVerify()) {
            return true
        }
        if (code.isNullOrBlank()) {
            return false
        }
        val split = code.split("-")
        if (split.size == 1) {
            return true
        }
        if (split.size < 2) {
            return false
        }
        val language = verifyAreaCode
        return split[1].equals(language, true)
    }

}