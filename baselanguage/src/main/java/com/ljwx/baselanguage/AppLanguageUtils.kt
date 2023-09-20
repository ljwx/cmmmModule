package com.ljwx.baselanguage

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.IntDef
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import java.util.LinkedList
import java.util.Locale

object AppLanguageUtils {

//    const val WEB_URL_PRIVACY = 1
//    const val WEB_URL_ARGUMENT = 2
//
//    @IntDef(WEB_URL_PRIVACY, WEB_URL_ARGUMENT)
//    @Retention(AnnotationRetention.SOURCE)
//    annotation class WebUrlType
//
//    private const val SP_NAME = "language_setting"
//    private const val KEY_LANGUAGE_OLD = "language_select"
//    private const val KEY_LANGUAGE = "key_language_symbol"
//
//    private val EN = "en" //英语
//    private val FR = "fr" //法语
//    private val DE = "de" //德语
//    private val IT = "it" //意大利
//    private val RU = "ru" //俄罗斯
//    private val ES = "es" //西班牙
//    private val PT = "pt" //葡萄牙
//    private val PL = "pl" //波兰
//    private val CS = "cs" //捷克
//    private val SK = "sk" //斯洛伐克
//
//    /**
//     * 默认值
//     */
//    private val DEFAULT_SYMBOL = EN
//    private val DEFAULT_HEADER = "en_US"
//
//    /**
//     * 各种语言标志
//     */
//    private val LANGUAGE_SYMBOL_HEADER =
//        linkedMapOf(
//            DEFAULT_SYMBOL to DEFAULT_HEADER,
//            DE to "de_DE",
//            RU to "ru_RU",
//            IT to "it_IT",
//            FR to "fr_FR",
//            ES to "es_ES",
//            PT to "es_PT",
//            PL to "pl_PL",
//            CS to "cs_CZ",
//            SK to "sk_SK",
//        )
//
//
//    /**
//     * 支持的语言列表
//     */
//    private val allLanguage by lazy {
//        val list = LinkedList<LanguageBean>()
//        val titles = StringUtils.getStringArray(R.array.language_list_title)
//        val contents = StringUtils.getStringArray(R.array.language_list_content)
//        titles.forEachIndexed { index, title ->
//            val content = if (contents.size > index) contents[index] else title
//            if (index < LANGUAGE_SYMBOL_HEADER.size) {
//                val symbol = LANGUAGE_SYMBOL_HEADER.keys.toList()[index]
//                val header = LANGUAGE_SYMBOL_HEADER[symbol]!!
//                list.add(LanguageBean(title, content, symbol, header))
//            }
//        }
//        list
//    }
//
//    /**
//     * 旧逻辑兼容
//     */
//    private fun convertOldCacheSymbol(): String? {
//        val type = SPUtils.getInstance(SP_NAME).getInt(KEY_LANGUAGE_OLD, -1)
//        if (type < 1) {
//            return null
//        }
//        return when (type) {
//            1 -> EN
//            2 -> FR
//            3 -> DE
//            4 -> IT
//            5 -> RU
//            else -> null
//        }
//    }
//
//    /**
//     * 获取存储的语言symbol
//     */
//    private fun getLanguageCache(): String {
//        val cache = SPUtils.getInstance(SP_NAME).getString(KEY_LANGUAGE, "")
//        if (cache.isNotEmpty()) {
//            return cache
//        }
//        val symbol = convertOldCacheSymbol()
//        symbol?.let {
//            return it
//        }
//        return SPUtils.getInstance(SP_NAME).getString(KEY_LANGUAGE, DEFAULT_SYMBOL)
//    }
//
//    /**
//     * 缓存选中语言
//     */
//    fun setLanguageCache(symbol: String) {
//        SPUtils.getInstance(SP_NAME).put(KEY_LANGUAGE, symbol, true)
//    }
//
//    /**
//     * 当前选中语言匹配
//     */
//    fun languageCheck() {
//        val symbol = getLanguageCache()
//        allLanguage.forEach {
//            it.isSelected = it.localeSymbol == symbol
//        }
//    }
//
//    /**
//     * 当前支持的所有语言
//     */
//    fun allLanguage(): List<LanguageBean> {
//        return allLanguage
//    }
//
//    /**
//     * 获取可用的语言对象,有可能用户没有选择语言,但手机默认不是英语
//     *
//     * @return APP支持的语言
//     */
//    @JvmStatic
//    fun getSelectLanguageBean(): LanguageBean {
//        val symbol = getLanguageCache()
//        //已手动选择
//        if (symbol != DEFAULT_SYMBOL) {
//            allLanguage.forEach {
//                if (it.localeSymbol == symbol) {
//                    return it
//                }
//            }
//        }
//        //未手选,系统语言是否在支持范围内
//        val systemLanguage = LanguageUtils.getSystemLanguage().language
//        if (systemLanguage in LANGUAGE_SYMBOL_HEADER.keys) {
//            LANGUAGE_SYMBOL_HEADER.keys.forEachIndexed { index, s ->
//                if (s == systemLanguage) {
//                    return allLanguage[index]
//                }
//            }
//        }
//        //默认英文
//        return allLanguage[0]
//    }
//
//    /**
//     * 获取可用的语言标记,有可能用户没有选择语言,但手机默认不是英语
//     *
//     * @return APP支持的语言
//     */
//    @JvmStatic
//    fun getSelectLanguageLocale(): Locale {
//        val symbol = getLanguageCache()
//        //已手动选择
//        if (symbol != DEFAULT_SYMBOL) {
//            return Locale(symbol)
//        }
//        //未手选,系统语言是否在支持范围内
//        val systemLanguage = LanguageUtils.getSystemLanguage().language
//        if (systemLanguage in LANGUAGE_SYMBOL_HEADER.keys) {
//            LANGUAGE_SYMBOL_HEADER.keys.forEachIndexed { index, s ->
//                if (s == systemLanguage) {
//                    return Locale(s)
//                }
//            }
//        }
//        //默认英文
//        return Locale(DEFAULT_SYMBOL)
//    }
//
//    /**
//     * 获取网络请求头语言标志
//     */
//    fun getRequestHeader(symbol: String): String {
//        return LANGUAGE_SYMBOL_HEADER[symbol] ?: DEFAULT_HEADER
//    }
//
//
//    /**
//     * 修改请求头
//     */
//    @JvmStatic
//    fun changeRequestHeader(languageSymbol: String? = null) {
//        val symbol = languageSymbol ?: getLanguageCache()
//        Constant.CURRENT_LANGUAGE = getRequestHeader(symbol)
//    }
//
//    /**
//     * 获取web页面url
//     */
//    @JvmStatic
//    fun getWebPrivacyUrl(@WebUrlType type: Int): String {
//        val domain = "https://protocol.sisensing.com/ce_sijoy/"
//        val type = if (type == WEB_URL_PRIVACY) "privacy" else "service"
//        val language = getSelectLanguageLocale().language.uppercase()
//        return domain + type + "_" + language + ".html"
//    }
//
//    /**
//     * 更新单个页面语言
//     */
//    @JvmStatic
//    fun updatePageConfiguration(context: Context) {
//        val configuration: Configuration = context.resources.configuration
//        val locale = getSelectLanguageLocale()
//        configuration.setLocale(locale)
//        context.createConfigurationContext(configuration)
//        setLanguageCache(locale.language)
//        LanguageUtils.applyLanguage(locale, false)
//    }
//
//    /**
//     * 当前页语言是否跟app设置的一样
//     */
//    @JvmStatic
//    fun currentIsMatch(context: Context): Boolean {
//        val current = context.resources.configuration.locale?.language
//        if (!current.isNullOrEmpty()) {
//            return getSelectLanguageLocale().language.equals(current)
//        }
//        return true
//    }
}