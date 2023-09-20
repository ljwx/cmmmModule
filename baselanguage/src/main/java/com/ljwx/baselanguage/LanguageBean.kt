package com.ljwx.baselanguage

data class LanguageBean(
    val title: String,
    val content: String,
    val localeSymbol: String,
    val requestHeader: String
) {

    var isSelected = false

}
