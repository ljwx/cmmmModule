package com.ljwx.basefragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ljwx.baseapp.debug.DebugUtils
import com.ljwx.baseapp.infochange.IBaseConfigInfo
import com.ljwx.baseapp.infochange.IBaseUserInfo
import com.ljwx.baseapp.page.IPageInfoChange
import com.ljwx.baseapp.vm.GlobalDataRepository

open class BaseToolsFragment : Fragment(), IPageInfoChange {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun userInfoChange(data: IBaseUserInfo?, type: Int) {

    }

    override fun configInfoChange(data: IBaseConfigInfo?, type: Int) {

    }

    open fun logCheckDebug() {
        logCheckDebugEx(DebugUtils.isDebug())
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}