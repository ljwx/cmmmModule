package com.ljwx.baseapp.vm

open class EmptyViewModel : BaseViewModel<EmptyDataRepository>() {
    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }


}