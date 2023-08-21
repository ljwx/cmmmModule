package com.ljwx.baselog.display

import java.util.Timer
import java.util.TimerTask

class LogDisplayInterval {

    private var timer: Timer? = Timer()

    private var task: TimerTask? = object : TimerTask() {
        override fun run() {

        }
    }

    fun start() {
        timer?.schedule(task, 1000, 2000)
    }

    fun destroy() {
        timer?.cancel()
        task?.cancel()
        timer = null
        task = null
    }

}