package com.ljwx.basemodule.debug

import android.util.Log
import com.ljwx.baseapp.debug.RunDebugProxy

class CustomRunDebug(type: Int) : RunDebugProxy(type) {

    override fun run() {
        super.run()
        Log.d(TAG, "custom run,type:$type")
    }

}