package com.ljwx.baseapp.util

import android.app.Application

object BaseAppUtils {

    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
    }

    fun getApplication(): Application {
        return application
    }

}