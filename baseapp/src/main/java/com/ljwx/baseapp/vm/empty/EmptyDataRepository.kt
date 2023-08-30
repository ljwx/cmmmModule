package com.ljwx.baseapp.vm.empty

import com.ljwx.baseapp.vm.model.BaseDataRepository

open class EmptyDataRepository : BaseDataRepository<Any>() {
    override fun createServer(): Any {
        return Any()
    }
}