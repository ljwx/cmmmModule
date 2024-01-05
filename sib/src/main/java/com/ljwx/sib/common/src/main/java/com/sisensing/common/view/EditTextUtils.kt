package com.sisensing.common.view

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils
import com.google.android.material.textfield.TextInputLayout
import com.sisensing.common.R
import com.sisensing.common.utils.Log

object EditTextUtils {

    const val NORMAL = 1
    const val FOCUS = 2
    const val ERROR = 3

    @JvmStatic
    fun focusChange(view: TextInputLayout) {
        if (view.hasFocus()) {
            setViewStroke(view, FOCUS)
        } else {
            setViewStroke(view, NORMAL)
        }
    }

    @JvmStatic
    fun focusChange(view: EditText, passed: Boolean) {
        if (view.hasFocus()) {
            setViewStroke(view, FOCUS)
        } else {
            if (passed) {
                setViewStroke(view, NORMAL)
            } else {
                setViewStroke(view, ERROR)
            }
        }
    }

    @JvmStatic
    fun showErrorStroke(view: View) {
        setViewStroke(view, ERROR)
    }

    fun setViewStroke(view: View, type: Int) {
        when (type) {
            NORMAL -> {
                view.background =
                    ContextCompat.getDrawable(
                        Utils.getApp(),
                        R.drawable.shape_edittext_stroke_normal
                    )
            }

            FOCUS -> {
                view.background =
                    ContextCompat.getDrawable(
                        Utils.getApp(),
                        R.drawable.shape_edittext_stroke_theme
                    )
            }

            ERROR -> {
                view.background =
                    ContextCompat.getDrawable(
                        Utils.getApp(),
                        R.drawable.shape_edittext_stroke_error
                    )
            }
        }
    }

}