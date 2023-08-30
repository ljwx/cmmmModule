package com.ljwx.baseapp.vm.model


abstract class BaseSecondRepository<Server, Server2> : BaseDataRepository<Server>() {

    protected val mApiServerSecond
        get() = createServerSecond()

    abstract fun createServerSecond(): Server2
}