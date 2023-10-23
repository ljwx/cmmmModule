package com.ljwx.sib.activity

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

/**
 * 获取Activity根布局
 */
val AppCompatActivity.rootLayout: View?
    get() {
        val container = findViewById<ViewGroup>(android.R.id.content)
        return if (container.childCount > 0) container.getChildAt(0) else container
    }