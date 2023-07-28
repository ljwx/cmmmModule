package com.ljwx.baseapp.vm

open class EmptyViewModel : BaseViewModel<EmptyDataRepository>() {

    open val TAG = this.javaClass.simpleName

    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }


}