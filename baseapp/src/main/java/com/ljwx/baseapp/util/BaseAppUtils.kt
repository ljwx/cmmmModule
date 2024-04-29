package com.ljwx.baseapp.util

import android.app.Application
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.Utils

object BaseAppUtils {

    private var application: Application? = null

    fun init(application: Application) {
        this.application = application
        Utils.init(application)
    }

    fun getApplication(): Application {
        return application ?: Utils.getApp()
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