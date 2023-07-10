package com.ljwx.baseactivity

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gyf.immersionbar.ImmersionBar

/**
 * 获取Activity根布局
 */
val AppCompatActivity.rootLayout: View?
    get() {
        val container = findViewById<ViewGroup>(android.R.id.content)
        return if (container.childCount > 0) container.getChildAt(0) else container
    }