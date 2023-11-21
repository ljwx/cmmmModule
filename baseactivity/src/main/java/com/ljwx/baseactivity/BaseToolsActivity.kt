package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.SensorEventListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.debug.ILogCheckRecyclerView
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.baseapp.shake.registerShake

open class BaseToolsActivity : AppCompatActivity() {

    private var sensorEventListener: SensorEventListener? = null

    private val screenStatusReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_USER_PRESENT -> {
                    }

                    Intent.ACTION_SCREEN_OFF -> {
                    }

                    Intent.ACTION_SCREEN_ON -> {
                    }
                }
            }
        }
    }

    open fun logCheck(open: Boolean) {
        val recycler = findViewById<View>(com.ljwx.baseapp.R.id.base_log_check_recycler) ?: return
        registerShake(open) {
            var visible = recycler.visibility == View.VISIBLE
            recycler.visibleGone(!visible)
        }
        if (open && recycler is ILogCheckRecyclerView) {
            recycler.run(lifecycleScope)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}