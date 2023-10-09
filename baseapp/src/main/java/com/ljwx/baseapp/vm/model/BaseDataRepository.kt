package com.ljwx.baseapp.vm.model

import com.ljwx.baseapp.response.BaseResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseDataRepository<Server> {

    companion object {

        private var observerOnError: ((e: Throwable) -> Unit)? = null

        private var responseFail: ((data: Any?) -> Unit)? = null

        /**
         * 通用的请求错误逻辑(代码执行错误)
         */
        fun setCommonOnError(onError: (e: Throwable) -> Unit) {
            observerOnError = onError
        }

        /**
         * 通用的请求失败逻辑(接口返回结果失败)
         */
        fun setCommonResponseFail(responseFail: (data: Any?) -> Unit) {
            Companion.responseFail = responseFail
        }
    }

    open val TAG = this.javaClass.simpleName

    private var mCompositeDisposable: CompositeDisposable? = null

    protected val mApiServer
        get() = createServer()


    abstract fun createServer(): Server

    /**
     * 页面销毁时,自动取消网络请求
     *
     * @param disposable Rxjava2版本的dispose
     */
    open fun autoClear2(disposable: io.reactivex.disposables.Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
    }

    /**
     * 页面销毁时,自动取消网络请求
     *
     * @param disposable Rxjava3版本的dispose
     */
    open fun autoClear3(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    /**
     * 自动取消
     */
    open fun onCleared() {
        mCompositeDisposable?.clear()
    }

    /**
     * RxJava3版本的结果监听
     */
    abstract inner class QuickObserver3<T> : io.reactivex.rxjava3.core.Observer<T> {
        override fun onSubscribe(d: Disposable) {
            autoClear3(d)
        }

        override fun onError(e: Throwable) {
            observerOnError?.invoke(e)
            onError()
        }

        override fun onComplete() {

        }

        override fun onNext(value: T) {
            if (value is BaseResponse<*>) {
                if (value.isSuccess()) {
                    onResponseSuccess(value)
                } else {
                    onResponseFail(value)
                }
            } else {
                onResult(value)
            }
        }

        /**
         * 接口数据成功
         *
         * @param value 接口返回结果
         */
        abstract fun onResponseSuccess(value: T)

        /**
         * 接口数据失败
         *
         * @param value 接口返回结果
         */
        open fun onResponseFail(value: T) {
            responseFail?.invoke(value)
        }

        /**
         * 非框架内的响应结构回调
         */
        open fun onResult(value: T?) {

        }

        /**
         * 异常错误回调
         */
        open fun onError() {

        }

    }

    /**
     * RxJava2版本的结果监听
     */
    abstract inner class QuickObserver<T> : io.reactivex.Observer<T> {
        override fun onSubscribe(d: io.reactivex.disposables.Disposable) {
            autoClear2(d)
        }

        override fun onError(e: Throwable) {
            observerOnError?.invoke(e)
            onError()
        }

        override fun onComplete() {

        }

        override fun onNext(value: T) {
            if (value is BaseResponse<*>) {
                if (value.isSuccess()) {
                    onResponseSuccess(value)
                } else {
                    onResponseFail(value)
                }
            } else {
                onResult(value)
            }
        }

        /**
         * 接口数据成功
         *
         * @param value 接口返回结果
         */
        abstract fun onResponseSuccess(value: T)

        /**
         * 接口数据失败
         *
         * @param value 接口返回结果
         */
        open fun onResponseFail(value: T) {
            responseFail?.invoke(value)
        }

        /**
         * 非框架内的响应结构回调
         */
        open fun onResult(value: T?) {

        }

        /**
         * 异常错误回调
         */
        open fun onError() {

        }

    }

}