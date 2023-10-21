package com.ljwx.baseapp.vm.model

import com.ljwx.baseapp.vm.IRxAutoCleared

interface IBaseDataRepository<Server> : IRxAutoCleared {

    fun createServer(): Server

}