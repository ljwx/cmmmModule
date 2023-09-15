package com.ljwx.baselanguage

import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

object LanguageNumberUtils {

    /**
     * 当前locale
     */
    val numberLocale by lazy {
//        AppLanguageUtils.getSelectLanguageLocale()
        Locale.US
    }

    /**
     * 当前语言小数分隔符
     */
    val decimalSeparator by lazy {
        DecimalFormatSymbols.getInstance(numberLocale).decimalSeparator
    }

    /**
     * 是否是小数判断正则
     */
    val decimalPattern by lazy {
        "[-+]?\\d*\\$decimalSeparator?\\d+"
    }

    /**
     * 当前语种格式化工具
     */
    private val format by lazy {
        NumberFormat.getInstance(numberLocale)
    }

    /**
     * 字符串转浮点
     */
    fun string2Float(decimalString: String?): Float? {
        if (decimalString.isNullOrBlank()) {
            return null
        }
        return try {
            val number = format.parse(decimalString)
            number.toFloat()
        } catch (e: ParseException) {
            null
        }
    }

}