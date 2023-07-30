package com.ljwx.basescaffold

import android.os.Handler
import android.os.Looper
import android.os.Message

class IntervalHandle {

    private val INTERVAL_WHAT = 0x001

    private val INTERVAL = 1000L

    private var millsCounted = 0L

    private var mObserver: OnTickAble? = null

    private val intervalHandler by lazy {
        object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                millsCounted += INTERVAL
                onTick()
                sendEmptyMessageDelayed(INTERVAL_WHAT, INTERVAL)
            }
        }
    }

    fun start() {
        if (mObserver == null) {
            return
        }
        millsCounted = 0L
        intervalHandler.sendEmptyMessageDelayed(INTERVAL_WHAT, INTERVAL)
    }

    fun stop() {
        intervalHandler.removeMessages(INTERVAL_WHAT)
    }

    fun setObserver(tick: OnTickAble) {
        mObserver = tick
    }

    private fun onTick() {
        mObserver?.onTick(millsCounted)
    }

    interface OnTickAble {
        fun onTick(mills: Long)
    }

}