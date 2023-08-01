package com.ljwx.baseswitchenv

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ljwx.baseapp.extensions.restart

fun AppCompatActivity.registerShakeEnv(debug: Boolean, callback: ShakeSelectAppEnv.EnvCallback) {
    if (debug) {
        lifecycle.addObserver(ShakeSelectAppEnv(this) {
            callback.selected(it)
            Toast.makeText(this, "选中${it.title} ,正在重启", Toast.LENGTH_LONG).show()
            restart()
        })
    }
}