package com.ljwx.baseapp.vm

open class EmptyDataRepository : BaseDataRepository<EmptyServer>() {
    override fun createServer(): EmptyServer {
        return object : EmptyServer {}
    }
}