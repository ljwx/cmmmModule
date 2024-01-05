package com.sisensing.common.utils

import com.sisensing.common.constants.Constant
import com.sisensing.common.user.UserInfoUtils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object GlucoseUtils {

    /**
     * 用户设置的血糖单位是否是mol
     */
    private var isUserConfigMol = true

    /**
     * 单位之间的比例
     */
    private const val mol2MgRatio = 18.016f

    /**
     * 血糖极值范围
     */
    private const val molMin = 2.2f
    private const val molMax = 25f
    private const val mgMin = 39f
    private const val mgMax = 450f

    /**
     * 小数格式
     */
    private fun getFormat(): DecimalFormat {
        return DecimalFormat("#0.0", DecimalFormatSymbols(LanguageNumberUtils.getNumberLocale()))
    }

    @JvmStatic
    fun initUnitFromUser() {
        isUserConfigMol = ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())
    }

    /**
     * 获取用户设置的单位的血糖值
     *
     * @param value 数值
     * @param valueIsMol 数值单位是否mol
     * @param limit 是否控制极限范围值,即小于2.2,显示2.2
     */
    @JvmOverloads
    @JvmStatic
    fun getUserConfigValue(value: Float?, valueIsMol: Boolean, limit: Boolean = false): String {
        if (value == null) {
            return ""
        }
        val floatResult = if (isUserConfigMol) getMol(value, valueIsMol, limit)
        else getMg(value, valueIsMol, limit)
        return if (floatResult == null) {
            ""
        } else {
            if (isUserConfigMol) getFormat().format(floatResult)
            else floatResult.toInt().toString()
        }
    }

    @JvmOverloads
    @JvmStatic
    fun getUserConfigValueWithUnit(
        value: Float?,
        valueIsMol: Boolean,
        limit: Boolean = false
    ): String {
        return getUserConfigValue(
            value,
            valueIsMol,
            limit
        ) + if (isUserConfigMol) BgUnitUtils.getMolUnit() else BgUnitUtils.getMgUnit()
    }

    /**
     * 获取直接单位的显示结果
     */
    @JvmStatic
    fun getConvertValue(
        value: Float?,
        valueIsMol: Boolean,
        resultIsMol: Boolean,
    ): String {
        if (value == null) {
            return ""
        }
        val floatResult = if (resultIsMol) getMol(value, valueIsMol)
        else getMg(value, valueIsMol)
        return if (floatResult == null) {
            ""
        } else {
            if (resultIsMol) getFormat().format(floatResult)
            else floatResult.toInt().toString()
        }
    }

    /**
     * 获取指定单位的显示结果
     */
    @JvmStatic
    fun getConvertValueWithUnit(
        value: Float?,
        valueIsMol: Boolean,
        resultIsMol: Boolean,
    ): String {
        return getConvertValue(value, valueIsMol, resultIsMol) + getUnit(resultIsMol)
    }

    /**
     * 获取浮点值
     */
    @JvmStatic
    fun getFloatValue(value: Float?, valueIsMol: Boolean, resultIsMol: Boolean): Float {
        if (value == null) {
            return 0.0f
        }
        return (if (resultIsMol) getMol(value, valueIsMol)
        else getMg(value, valueIsMol)) ?: 0.0f
    }

    /**
     * 比较最小值
     */
    @JvmStatic
    fun lessThanMin(source: String?, sourceIsMol: Boolean): Boolean {
        if (source.isNullOrBlank()) {
            return true
        }
        val sourceF = LanguageNumberUtils.string2Float(source)
        if (sourceIsMol) {
            return sourceF < molMin
        } else {
            return sourceF < mgMin
        }
    }

    /**
     * 比较最大值
     */
    @JvmStatic
    fun moreThanMax(source: String?, sourceIsMol: Boolean): Boolean {
        if (source.isNullOrBlank()) {
            return true
        }
        val sourceF = LanguageNumberUtils.string2Float(source)
        if (sourceIsMol) {
            return sourceF > molMax
        } else {
            return sourceF > mgMax
        }
    }

    /**
     * 获取mg值
     * @param value 数值
     * @param valueIsMol 是否是mol值
     * @param limit 是否控制范围
     */
    fun getMg(value: Float?, valueIsMol: Boolean, limit: Boolean = false): Float? {
        if (value == null) {
            return null
        }
        if (limit) {
            val rangeValue = rangeValue(value, valueIsMol)
            if (rangeValue != null) {
                return rangeValue
            }
        }
        return if (valueIsMol) {
            mol2Mg(value)
        } else {
            value
        }
    }

    /**
     * 获取mol值
     *
     * @param value 数值
     * @param valueIsMol 是否是mol值
     * @param limit 是否控制范围
     */
    fun getMol(value: Float?, valueIsMol: Boolean, limit: Boolean = false): Float? {
        if (value == null) {
            return null
        }
        if (limit) {
            val rangeValue = rangeValue(value, valueIsMol)
            if (rangeValue != null) {
                return rangeValue
            }
        }
        return if (valueIsMol) {
            value
        } else {
            mg2Mol(value)
        }
    }

    /**
     * 获取单位显示
     */
    fun getUnit(isMol: Boolean): String {
        return if (isMol) BgUnitUtils.getMolUnit() else BgUnitUtils.getMgUnit()
    }

    /**
     * mol转mg
     */
    private fun mol2Mg(mol: Float): Float {
        return (mol * mol2MgRatio)
    }

    /**
     * mg转mol
     */
    private fun mg2Mol(mg: Float): Float {
        return mg / mol2MgRatio
    }

    /**
     * 显示个人信息中的高低血糖
     *
     * 猜测,个人信息中保存的是mg值,所以显示mol时,需反向转换
     */
    @JvmStatic
    fun getUserHighLowBg(high: Boolean, isMol: Boolean): String {
        val value = getHighLowFromUserInfo(high)
        return if (isMol) getFormat().format(value / mol2MgRatio) else value.toInt().toString()
    }

    /**
     * 获取个人信息中的高低血糖值
     */
    private fun getHighLowFromUserInfo(high: Boolean): Float {
        UserInfoUtils.getPersonalInfo()?.apply {
            //设定值
            alarm?.apply {
                return if (high) upper else lower
            }
            //对应默认值
            return getBsRangeValue(high, drType)
        }
        return if (high) Constant.BS_UPPER_DEFAULT else Constant.BS_LOWER_DEFAULT
    }

    /**
     * 各种糖尿病类型的对应高低血糖值
     */
    private fun getBsRangeValue(upper: Boolean, drType: Int): Float {
        //drType 0: 未诊断/无糖尿病史
        // 1: I型糖尿病   2: II型糖尿病 3: 特殊类型糖尿病  4: 妊娠期糖尿病 5:Elderly diabetes 6 High-risk diabetes 7:其他
        return if (upper) {
            when (drType) {
                1, 2 -> Constant.BS_UPPER_TYPE_1_2
                4 -> Constant.BS_UPPER_TYPE_GESTATION
                5, 6 -> Constant.BS_UPPER_TYPE_ELDER_HIGH
                else -> Constant.BS_UPPER_DEFAULT
            }
        } else {
            when (drType) {
                4 -> Constant.BS_LOWER_GESTATION
                5, 6 -> Constant.BS_LOWER_ELDER_HIGH
                else -> Constant.BS_LOWER_DEFAULT
            }
        }
    }

    /**
     * 数值范围控制
     */
    private fun rangeValue(value: Float, isMol: Boolean): Float? {
        if (isMol) {
            if (value < molMin) {
                return molMin
            }
            if (value > molMax) {
                return molMax
            }
        } else {
            if (value < mgMin) {
                return mgMin
            }
            if (value > mgMax) {
                return mgMax
            }
        }
        return null
    }

}