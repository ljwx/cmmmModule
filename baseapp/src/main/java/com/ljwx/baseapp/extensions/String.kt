package com.ljwx.baseapp.extensions

inline fun String?.isDigit() {

}

inline fun CharSequence?.notNullOrBlank(): Boolean {
    return !isNullOrBlank()
}