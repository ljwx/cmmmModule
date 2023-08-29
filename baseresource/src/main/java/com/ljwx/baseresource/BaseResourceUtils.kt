package com.ljwx.baseresource

import android.content.Context
import androidx.core.content.ContextCompat

object BaseResourceUtils {

    fun setThemeColor(context: Context) {
        val themeColor = ContextCompat.getColor(context, R.color.base_resource_theme_color)
    }

}