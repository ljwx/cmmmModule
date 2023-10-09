package com.ljwx.baseapp.vm.empty

import com.ljwx.baseapp.vm.BaseViewModel

open class EmptyViewModel : BaseViewModel<EmptyDataRepository>() {

    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }


}