package com.ljwx.baseapp.vm

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseDataRepository<Server> {

    companion object {

        fun retrofitInstance() {

        }
    }

    private var mCompositeDisposable: CompositeDisposable? = null

    protected val mApiServer
        get() = createServer()


    abstract fun createServer(): Server

    open fun autoClear2(disposable: io.reactivex.disposables.Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
    }

    open fun autoClear3(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    open fun onCleared() {
        mCompositeDisposable?.clear()
    }

}