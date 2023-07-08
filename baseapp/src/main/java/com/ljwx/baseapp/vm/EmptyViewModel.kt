package com.ljwx.baseapp.vm

class EmptyViewModel : BaseViewModel<EmptyDataRepository>() {
    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }


}