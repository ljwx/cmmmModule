package com.ljwx.baseactivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ljwx.baseapp.debug.DebugUtils
import com.ljwx.baseapp.infochange.IBaseConfigInfo
import com.ljwx.baseapp.page.IPageInfoChange
import com.ljwx.baseapp.infochange.IBaseUserInfo
import com.ljwx.baseapp.vm.GlobalDataRepository

open class BaseToolsActivity : AppCompatActivity(), IPageInfoChange {

    private val Tag = this.javaClass.simpleName + "-[page"

    protected open var enableUserInfoChangeListener = false
    protected open var enableConfigInfoChangeListener = false
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
            GlobalDataRepository.observeUserInfo(this) {
                userInfoChange(it, it?.getInfoChangeType() ?: 0)
            }
        }
        if (enableConfigInfoChangeListener) {
            GlobalDataRepository.observeConfigInfo(this) {
                configInfoChange(it, it?.getInfoChangeType() ?: 0)
            }
        }
    }

    open fun logCheckDebug() {
        logCheckDebugEx(DebugUtils.isDebug())
    }

    override fun userInfoChange(data: IBaseUserInfo?, type: Int) {
        Log.d(Tag, "监测到用户信息改变")
    }

    override fun configInfoChange(data: IBaseConfigInfo?, type: Int) {
        Log.d(Tag, "监测到配置信息改变")
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}