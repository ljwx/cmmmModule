package com.sisensing.common

data class LanguageBean(
    val title: String,
    val content: String,
    val localeSymbol: String,
    val requestHeader: String
) {

    var isSelected = false

}
