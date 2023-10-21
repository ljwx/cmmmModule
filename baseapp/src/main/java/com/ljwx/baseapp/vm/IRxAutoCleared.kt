package com.ljwx.baseapp.vm

interface IRxAutoCleared {

    fun autoClear(disposable: io.reactivex.disposables.Disposable)

    fun autoClear(disposable: io.reactivex.rxjava3.disposables.Disposable)

    fun onRxCleared()

}