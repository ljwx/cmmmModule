package com.sisensing.common.utils

import com.sisensing.common.user.UserInfoUtils

object GlucoseDrTypeUtils {

    const val UN_DIAGNOSED = 0
    const val OTHER_DIABETES = 7
    const val I_DIABETES = 1
    const val II_DIABETES = 2
    const val SPECIAL_DIABETES = 3
    const val GESTATIONAL_PERIOD_DIABETES = 4 //妊娠
    const val ELDERLY_DIABETES = 5 //老年
    const val HIGH_RISK_DIABETES = 6 //高风险

    const val rate = 18.016f

    @JvmStatic
    fun getVeryLowMol(): Float? {
        val type = UserInfoUtils.getDrType()
        val rate: Number = if (BgUnitUtils.isUserMol()) 1 else rate
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return null
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return 3.0f
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return 3.0f
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return null
            }
        }
        return null
    }

    @JvmStatic
    fun getLowMol(): Float? {
        val type = UserInfoUtils.getDrType()
        val rate: Number = if (BgUnitUtils.isUserMol()) 1 else rate
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return 3.9f
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return 3.9f
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return 3.5f
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return 3.9f
            }
        }
        return null
    }

    @JvmStatic
    fun getHighMol(): Float? {
        val type = UserInfoUtils.getDrType()
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return 7.8f
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return 10.0f
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return 7.8f
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return 10.0f
            }
        }
        return null
    }

    @JvmStatic
    fun getVeryHighMol(): Float? {
        val type = UserInfoUtils.getDrType()
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return null
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return 13.9f
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return null
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return 13.9f
            }
        }
        return null
    }

    @JvmStatic
    fun getLimitMol(): FloatArray {
        val type = UserInfoUtils.getDrType()
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return floatArrayOf(3.9f, 7.8f)
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return floatArrayOf(3.0f, 3.9f, 10.0f, 13.9f)
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return floatArrayOf(3.0f, 3.5f, 7.8f)
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return floatArrayOf(3.9f, 10.0f, 13.9f)
            }
        }
        return FloatArray(0)
    }

    @JvmStatic
    fun getLimitMg(): IntArray {
        val type = UserInfoUtils.getDrType()
        when (type) {
            UN_DIAGNOSED, OTHER_DIABETES -> {
                return intArrayOf((3.9f * rate).toInt(), (7.8f * rate).toInt())
            }

            I_DIABETES, II_DIABETES, SPECIAL_DIABETES -> {
                return intArrayOf(
                    (3.0f * rate).toInt(),
                    (3.9f * rate).toInt(),
                    (10.0f * rate).toInt(),
                    (13.9f * rate).toInt()
                )
            }

            GESTATIONAL_PERIOD_DIABETES -> {
                return intArrayOf(
                    (3.0f * rate).toInt(),
                    (3.5f * rate).toInt(),
                    (7.8f * rate).toInt()
                )
            }

            ELDERLY_DIABETES, HIGH_RISK_DIABETES -> {
                return intArrayOf(
                    (3.9f * rate).toInt(),
                    (10.0f * rate).toInt(),
                    (13.9f * rate).toInt()
                )
            }
        }
        return intArrayOf(0)
    }

}