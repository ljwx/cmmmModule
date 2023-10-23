package com.ljwx.baseapp.vm.empty

import android.app.Application
import com.ljwx.baseapp.vm.BaseAndroidViewModel

open class EmptyAndroidViewModel(application: Application) :
    BaseAndroidViewModel<EmptyDataRepository>(application) {

    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }


}