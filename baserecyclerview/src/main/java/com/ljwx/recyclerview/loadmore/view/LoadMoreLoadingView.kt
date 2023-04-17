package com.ljwx.recyclerview.loadmore.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar

class LoadMoreLoadingView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    style: Int = 0,
) : FrameLayout(context, attributeSet, style) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val padding = 36
        setPadding(padding, padding, padding, padding)
        addProgressBar()
    }

    private fun addProgressBar() {
        val size = 120
        val params = LayoutParams(size, size)
        params.gravity = Gravity.CENTER
        addView(ProgressBar(context), params)
    }
}