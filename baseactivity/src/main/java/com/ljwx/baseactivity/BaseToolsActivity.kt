package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.SensorEventListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ljwx.baseapp.debug.DebugUtils
import com.ljwx.baseapp.page.IPageInfoChange
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.GlobalDataRepository

open class BaseToolsActivity : AppCompatActivity() ,IPageInfoChange{

    protected open var enableUserInfoChangeListener = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableUserInfoChangeListener) {
            GlobalDataRepository.observeUserInfo(this){
                userInfoChange(it)
            }
        }
    }

    open fun logCheckDebug() {
        logCheckDebugEx(DebugUtils.isDebug())
    }

    override fun <T> userInfoChange(data: T, type: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}