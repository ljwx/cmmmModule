package com.sisensing.common.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.sisensing.common.LanguageBean
import com.sisensing.common.R
import com.sisensing.common.constants.Constant
import java.text.DateFormat
import java.util.LinkedList
import java.util.Locale

object AppLanguageUtils {

    const val WEB_URL_PRIVACY = 1
    const val WEB_URL_ARGUMENT = 2
    const val PRIVACY_VERSION = "v1"

    @IntDef(WEB_URL_PRIVACY, WEB_URL_ARGUMENT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class WebUrlType

    private const val SP_NAME = "language_setting"
    private const val KEY_LANGUAGE_OLD = "language_select"
    private const val KEY_LANGUAGE = "key_language_symbol"

    private val EN = "en" //英语
    private val DE = "de" //德语
    private val RU = "ru" //俄罗斯
    private val IT = "it" //意大利
    private val FR = "fr" //法语
    private val ES = "es" //西班牙
    private val PT = "pt" //葡萄牙
    private val PL = "pl" //波兰
    private val CS = "cs" //捷克
    private val SK = "sk" //斯洛伐克

    /**
     * 默认值
     */
    private val DEFAULT_SYMBOL = EN
    private val DEFAULT_HEADER = "en_US"

    /**
     * 各种语言标志
     */
    private val LANGUAGE_SYMBOL_HEADER =
        linkedMapOf(
            DEFAULT_SYMBOL to DEFAULT_HEADER,
            DE to "de_DE",
            RU to "ru_RU",
            IT to "it_IT",
            FR to "fr_FR",
            ES to "es_ES",
            PT to "pt_PT",
            PL to "pl_PL",
            CS to "cs_CZ",
            SK to "sk_SK",
        )

    private val LANGUAGE_SWITCH = linkedMapOf(
        DEFAULT_SYMBOL to 0,
        DE to 1,
        RU to 2,
        IT to 3,
        FR to 4,
        ES to 5,
        PT to 6,
        PL to 7,
        CS to 8,
        SK to 9,
    )

    /**
     * 支持的语言列表
     */
//    private val allLanguage by lazy {
//        val list = LinkedList<LanguageBean>()
//        val titles = StringUtils.getStringArray(R.array.language_list_title)
//        val contents = StringUtils.getStringArray(R.array.language_list_content)
//        titles.forEachIndexed { index, title ->
//            if (index in LANGUAGE_SWITCH.values) {
//                val content = if (contents.size > index) contents[index] else title
//                if (index < LANGUAGE_SYMBOL_HEADER.size) {
//                    val symbol = LANGUAGE_SYMBOL_HEADER.keys.toList()[index]
//                    val header = LANGUAGE_SYMBOL_HEADER[symbol]!!
//                    list.add(LanguageBean(title, content, symbol, header))
//                }
//            }
//        }
//        list
//    }

    private fun initAllLanguage(): LinkedList<LanguageBean> {
        val list = LinkedList<LanguageBean>()
        val titles = StringUtils.getStringArray(R.array.language_list_title)
        val contents = StringUtils.getStringArray(R.array.language_list_content)
        val cacheSymbol = getLanguageCache()
        titles.forEachIndexed { index, title ->
            if (index in LANGUAGE_SWITCH.values) {
                val content = if (contents.size > index) contents[index] else title
                if (index < LANGUAGE_SYMBOL_HEADER.size) {
                    val symbol = LANGUAGE_SYMBOL_HEADER.keys.toList()[index]
                    val header = LANGUAGE_SYMBOL_HEADER[symbol]!!
                    val bean = LanguageBean(title, content, symbol, header)
                    bean.isSelected = bean.localeSymbol == cacheSymbol
                    list.add(bean)
                }
            }
        }
        return list
    }

    /**
     * 旧逻辑兼容
     */
    private fun convertOldCacheSymbol(): String? {
        val type = SPUtils.getInstance(SP_NAME).getInt(KEY_LANGUAGE_OLD, -1)
        if (type < 1) {
            return null
        }
        return when (type) {
            1 -> EN
            2 -> FR
            3 -> DE
            4 -> IT
            5 -> RU
            else -> null
        }
    }

    /**
     * 获取存储的语言symbol
     */
    private fun getLanguageCache(): String {
        val cache = SPUtils.getInstance(SP_NAME).getString(KEY_LANGUAGE, "")
        if (cache.isNotEmpty()) {
            return cache
        }
        val symbol = convertOldCacheSymbol()
        symbol?.let {
            return it
        }
        return SPUtils.getInstance(SP_NAME).getString(KEY_LANGUAGE, DEFAULT_SYMBOL)
    }

    /**
     * 缓存选中语言
     */
    fun setLanguageCache(symbol: String) {
        SPUtils.getInstance(SP_NAME).put(KEY_LANGUAGE, symbol, true)
    }

    /**
     * 当前支持的所有语言
     */
    fun allLanguage(): List<LanguageBean> {
        return initAllLanguage()
    }

    /**
     * 获取可用的语言对象,有可能用户没有选择语言,但手机默认不是英语
     *
     * @return APP支持的语言
     */
    @JvmStatic
    fun getSelectLanguageBean(): LanguageBean {
        val symbol = getLanguageCache()
        //已手动选择
        if (symbol != DEFAULT_SYMBOL) {
            initAllLanguage().forEach {
                if (it.localeSymbol == symbol) {
                    return it
                }
            }
        }
        //未手选,系统语言是否在支持范围内
        val systemLanguage = LanguageUtils.getSystemLanguage().language
        if (systemLanguage in LANGUAGE_SYMBOL_HEADER.keys) {
            LANGUAGE_SYMBOL_HEADER.keys.forEachIndexed { index, s ->
                if (s == systemLanguage) {
                    return allLanguage()[index]
                }
            }
        }
        //默认英文
        return allLanguage()[0]
    }

    /**
     * 获取可用的语言标记,有可能用户没有选择语言,但手机默认不是英语
     *
     * @return APP支持的语言
     */
    @JvmStatic
    fun getSelectLanguageLocale(): Locale {
        val symbol = getLanguageCache()
        //已手动选择
        if (symbol != DEFAULT_SYMBOL) {
            if (symbol in LANGUAGE_SWITCH.keys) {
                return Locale(symbol)
            } else {
                return Locale(DEFAULT_SYMBOL)
            }
        }
        //未手选,系统语言是否在支持范围内
        val systemLanguage = LanguageUtils.getSystemLanguage().language
        if (systemLanguage in LANGUAGE_SYMBOL_HEADER.keys) {
            LANGUAGE_SYMBOL_HEADER.keys.forEachIndexed { index, s ->
                if (s == systemLanguage) {
                    return Locale(s)
                }
            }
        }
        //默认英文
        return Locale(DEFAULT_SYMBOL)
    }

    /**
     * 获取网络请求头语言标志
     */
    fun getRequestHeader(symbol: String): String {
        return LANGUAGE_SYMBOL_HEADER[symbol] ?: DEFAULT_HEADER
    }


    /**
     * 修改请求头
     */
    @JvmStatic
    fun changeRequestHeader(languageSymbol: String? = null) {
        val symbol = languageSymbol ?: getLanguageCache()
        Constant.CURRENT_LANGUAGE = getRequestHeader(symbol)
    }

    /**
     * 获取web页面url
     */
    @JvmStatic
    fun getWebPrivacyUrl(@WebUrlType type: Int): String {

        val domain =
            "https://cgm-ce-public.s3.eu-central-1.amazonaws.com/policy/app/"
        val version =
            if (type == WEB_URL_PRIVACY) Constant.PRIVACY_VERSION else Constant.ARGUMENT_VERSION
        val type = if (type == WEB_URL_PRIVACY) "privacy_policy" else "user_agreement"
        val language = getSelectLanguageLocale().language.uppercase()
        val url = domain + version + "/" + type + "_" + language + ".html"
        Log.d("隐私协议", "url地址:$url")
        return url
    }

    /**
     * 更新单个页面语言
     */
    @JvmStatic
    fun updatePageConfiguration(context: Context) {
        val configuration: Configuration = context.resources.configuration
        val locale = getSelectLanguageLocale()
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        setLanguageCache(locale.language)
        LanguageUtils.applyLanguage(locale, false)
    }

    @JvmStatic
    fun updateContext(context: Context) {
        val configuration: Configuration = context.resources.configuration
        val locale = getSelectLanguageLocale()
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
    }

    /**
     * 当前页语言是否跟app设置的一样
     */
    @JvmStatic
    fun currentIsMatch(context: Context): Boolean {
        val current = context.resources.configuration.locale?.language
        Log.d("多语言", "当前页面语言:$current")
        if (!current.isNullOrEmpty()) {
            return getSelectLanguageLocale().language.equals(current)
        }
        return true
    }

    @JvmStatic
    @JvmOverloads
    fun activityLanguage(activity: Activity, tag: String? = null) {
        val t = tag ?: activity.javaClass.simpleName
        if (!currentIsMatch(activity)) {
            Log.d("多语言不匹配", "$t,重建")
            updatePageConfiguration(activity)
            activity.recreate()
        }
    }

    @JvmStatic
    fun fragmentLanguage(fragment: Fragment) {
        val configuration: Configuration = fragment.resources.configuration
        configuration.setLocale(getSelectLanguageLocale())
        fragment.resources.updateConfiguration(configuration, fragment.resources.displayMetrics)
    }

    @JvmStatic
    fun isMYD(context: Context): Boolean {
        // 获取日期格式设置
        val dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, getSelectLanguageLocale())
        // 获取日期格式字符串
        val dateFormatPattern = dateFormat.toString()
        // 检查日期格式字符串中的顺序
        Log.d("多语言", dateFormatPattern)
        return dateFormatPattern.indexOf("d") < dateFormatPattern.indexOf("M")
    }

}