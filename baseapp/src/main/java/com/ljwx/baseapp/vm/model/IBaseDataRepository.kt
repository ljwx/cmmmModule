package com.ljwx.baseapp.vm.model

import io.reactivex.rxjava3.disposables.Disposable

interface IBaseDataRepository<Server> {

    fun createServer(): Server

    fun autoClear(disposable: io.reactivex.disposables.Disposable)

    fun autoClear(disposable: Disposable)

    fun onCleared()

}