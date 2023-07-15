package com.ljwx.baseapp.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

private val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun View.showSoftInput(): Boolean {
    return context.inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.showSoftInput(): Boolean {
    return currentFocus?.showSoftInput() ?: false
}

fun View.hideSoftInput(): Boolean {
    return context.inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.hideSoftInput(): Boolean {
    return currentFocus?.hideSoftInput() ?: false
}