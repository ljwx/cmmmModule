package com.ljwx.recordcheck

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

object MultiWindowUtils {

    @RequiresApi(Build.VERSION_CODES.N)
    fun startMultiWindowActivity(activity: Activity, target: Class<*>) {
        val intent = Intent(activity, target)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
        val options = MultiWindowUtils.getActivityOptions(1)
        options.launchBounds = Rect(0, 0, 300, 300)
        val bundle = options.toBundle()
        activity.startActivity(intent, bundle)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getActivityOptions(type: Int): ActivityOptions {
        val options = ActivityOptions.makeBasic()
        try {
            val setDockCreateMode =
                ActivityOptions::class.java.getMethod("setDockCreateMode", Integer.TYPE)
            setDockCreateMode?.invoke(options, 1)
            val setLaunchStackId =
                ActivityOptions::class.java.getMethod("setLaunchStackId", Integer.TYPE)
            setLaunchStackId.invoke(options, 3)
        } catch (e: Exception) {
            Log.e("MultiWindowError", e.toString())
        }
        return options
    }

}