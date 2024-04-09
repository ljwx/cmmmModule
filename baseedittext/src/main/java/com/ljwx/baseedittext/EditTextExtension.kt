package com.ljwx.baseedittext

import android.widget.EditText
import androidx.annotation.StringRes

fun EditText.validOrDefault(defaultContent: String): String {
    val filter = text.toString().replace("^[\\n\\r]+", "").replace("[\\n\\r]+$", "").trim()
    return if (filter.isEmpty()) {
        defaultContent
    } else {
        filter
    }
}

fun EditText.validOrDefault(@StringRes id: Int): String {
    val filter = text.toString().replace("^[\\n\\r]+", "").replace("[\\n\\r]+$", "").trim()
    return if (filter.isEmpty()) {
        context.getString(id)
    } else {
        filter
    }
}