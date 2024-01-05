package com.sisensing.common.utils

import androidx.core.text.isDigitsOnly
import com.blankj.utilcode.util.RegexUtils
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

object LanguageNumberUtils {

//    /**
//     * 当前语言小数分隔符
//     */
//    private val decimalSeparator by lazy {
//        DecimalFormatSymbols.getInstance(getNumberLocale()).decimalSeparator
//    }
//
//    /**
//     * 是否是小数判断正则
//     */
//    private val decimalPattern by lazy {
//        "[-+]?\\d*\\$decimalSeparator?\\d+"
//    }

    /**
     * 当前locale
     */
    fun getNumberLocale(): Locale {
        return AppLanguageUtils.getSelectLanguageLocale()
    }


    /**
     * 当前语种格式化工具
     */
    private fun getFormat(): NumberFormat {
        return NumberFormat.getInstance(getNumberLocale())
    }

    private val USFormat by lazy {
        NumberFormat.getInstance(Locale.US)
    }

    private val FRFormat by lazy {
        NumberFormat.getInstance(Locale.FRENCH)
    }

    /**
     * 字符串转浮点
     */
    @JvmStatic
    @JvmOverloads
    fun string2Float(decimalString: String?, default: Float = -1f): Float {
        if (decimalString.isNullOrBlank()) {
            return default
        }
        if (decimalString.contains(".")) {
            return try {
                val number = USFormat.parse(decimalString)
                number.toFloat()
            } catch (e: ParseException) {
                default
            }
        } else if (decimalString.contains(",")) {
            return try {
                val number = FRFormat.parse(decimalString)
                number.toFloat()
            } catch (e: ParseException) {
                default
            }
        } else if(decimalString.isDigitsOnly()){
            return decimalString.toFloat()
        }
        return default
    }

    @JvmStatic
    fun floatDisplay(float: Float?): String {
        if (float == null) {
            return ""
        }
        return getFormat().format(float)
    }

}