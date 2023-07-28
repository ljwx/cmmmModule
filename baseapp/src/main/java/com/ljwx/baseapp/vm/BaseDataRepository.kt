package com.ljwx.baseapp.vm

import com.ljwx.baseapp.response.BaseResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseDataRepository<Server> {

    companion object {

        private var observerOnError: ((e: Throwable) -> Unit)? = null

        private var responseFail: ((data: Any?) -> Unit)? = null

        fun setCommonOnError(onError: (e: Throwable) -> Unit) {
            observerOnError = onError
        }

        fun setCommonResponseFail(responseFail: (data: Any?) -> Unit) {
            this.responseFail = responseFail
        }
    }

    open val TAG = this.javaClass.simpleName

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

    open fun commonSuccess() {

    }

    abstract inner class QuickObserver3<T> : io.reactivex.rxjava3.core.Observer<T> {
        override fun onSubscribe(d: Disposable) {
            autoClear3(d)
        }

        override fun onError(e: Throwable) {
            observerOnError?.invoke(e)
        }

        override fun onComplete() {

        }

        override fun onNext(value: T) {
            if (value is BaseResponse<*>) {
                if (value.isSuccessAndDataNotNull()) {
                    onResponseSuccess(value)
                } else {
                    onResponseFail(value)
                }
            }
        }

        abstract fun onResponseSuccess(value: T)

        open fun <T : Any?> onResponseFail(value: T?) {
            responseFail?.invoke(value)
        }
    }

    abstract inner class QuickObserver<T> : io.reactivex.Observer<T> {
        override fun onSubscribe(d: io.reactivex.disposables.Disposable) {
            autoClear2(d)
        }

        override fun onError(e: Throwable) {
            observerOnError?.invoke(e)
        }

        override fun onComplete() {

        }

        override fun onNext(value: T) {
            if (value is BaseResponse<*>) {
                if (value.isSuccessAndDataNotNull()) {
                    onResponseSuccess(value)
                } else {
                    onResponseFail(value)
                }
            }
        }

        abstract fun <T : Any> onResponseSuccess(value: T)

        open fun <T : Any?> onResponseFail(value: T?) {
            responseFail?.invoke(value)
        }

    }

}