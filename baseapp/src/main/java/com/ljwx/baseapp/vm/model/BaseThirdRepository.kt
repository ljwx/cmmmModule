package com.ljwx.baseapp.vm.model


abstract class BaseThirdRepository<Server, Server2, Server3> : BaseSecondRepository<Server, Server2>() {

    protected val mApiServerThird
        get() = createServerThird()

    abstract fun createServerThird(): Server3
}