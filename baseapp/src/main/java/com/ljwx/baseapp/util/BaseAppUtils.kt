package com.ljwx.baseapp.util

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner

object BaseAppUtils {

    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
    }

    fun getApplication(): Application {
        return application
    }

    fun getApplicationViewModelStore(): ViewModelStoreOwner {
        if (application is ViewModelStoreOwner) {
            return application as ViewModelStoreOwner
        }
        //1.application implements ViewModelStoreOwner
        //2.onCreate{ mAppViewModelStore = new ViewModelStore() }
        //3.public ViewModelStore getViewModelStore() {
        //    return mAppViewModelStore;
        //}
        throw IllegalAccessException("Application need implement ViewModelStoreOwner")
    }

}