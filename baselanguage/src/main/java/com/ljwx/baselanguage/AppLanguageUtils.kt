package com.ljwx.baselanguage

import android.content.Context
import android.content.res.Configuration
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import java.util.Locale

object AppLanguageUtils {


//    private const val DEFAULT_SYMBOL = "en"
//    private const val DEFAULT_HEADER = "en_US"
//
//    private const val SP_NAME = "language_setting"
//    private const val KEY_LANGUAGE = "language_select"
//
//    /**
//     * 各种语言标志
//     */
//    val LANGUAGE_SYMBOL_HEADER =
//        mutableMapOf(
//            DEFAULT_SYMBOL to DEFAULT_HEADER,
//            "fr" to "fr_FR",
//            "de" to "de_DE",
//            "it" to "it_IT",
//            "ru" to "ru_RU",
//            "es" to "es_ES",
//        )
//
//    /**
//     * 支持的语言列表
//     */
//    private val allLanguage by lazy {
//        val list = ArrayList<LanguageBean>()
//        val titles = StringUtils.getStringArray(R.array.language_list_title)
//        val contents = StringUtils.getStringArray(R.array.language_list_content)
//        titles.forEachIndexed { index, title ->
//            val content = if (contents.size > index) contents[index] else title
//            list.add(LanguageBean(title, content, index + 1))
//        }
//        list
//    }
//
//    /**
//     * 获取存储的语言type
//     */
//    private fun getLanguageCache(): Int {
//        return SPUtils.getInstance(SP_NAME).getInt(KEY_LANGUAGE, -1)
//    }
//
//    /**
//     * 缓存选中语言
//     */
//    fun setLanguageCache(typeOrPosition: Int) {
//        SPUtils.getInstance(SP_NAME).put(KEY_LANGUAGE, typeOrPosition)
//    }
//
//    /**
//     * 当前选中语言匹配
//     */
//    fun languageCheck() {
//        val checkType = getLanguageCache()
//        allLanguage.forEach {
//            it.isSelected = it.typeOrPosition == checkType
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
//        val type = getLanguageCache()
//        //已手动选择
//        if (type != -1) {
//            return allLanguage[type - 1]
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
//    fun getSelectLanguageLocale(): Locale {
//        val type = getLanguageCache()
//        //已手动选择
//        if (type != -1) {
//            val symbol = ArrayList(LANGUAGE_SYMBOL_HEADER.keys)[type - 1]
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
//    fun getRequestHeader(typeOrPosition: Int): String {
//        if (typeOrPosition <= LANGUAGE_SYMBOL_HEADER.size) {
//            return ArrayList(LANGUAGE_SYMBOL_HEADER.values)[typeOrPosition - 1]
//        }
//        return DEFAULT_HEADER
//    }
//
//    /**
//     * 更新单个页面语言
//     */
//    @JvmStatic
//    fun updatePageConfiguration(context: Context) {
//        val configuration: Configuration = context.resources.configuration
//        configuration.setLocale(getSelectLanguageLocale())
//        context.createConfigurationContext(configuration)
//    }
//
//    /**
//     * 当前页语言是否跟app设置的一样
//     */
//    @JvmStatic
//    fun currentIsMatch(context: Context) :Boolean{
//        val current = context.resources.configuration.locale?.language
//        if (!current.isNullOrEmpty()) {
//            return getSelectLanguageLocale().language.equals(current)
//        }
//        return true
//    }

}