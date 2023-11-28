package com.ljwx.baseservice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BaseService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return Binder()
    }
}