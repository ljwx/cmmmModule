package com.ljwx.baseapp.vm.model

import com.ljwx.baseapp.constant.BaseLogTag
import com.ljwx.baseapp.extensions.toIntSafe
import com.ljwx.baseapp.response.BaseResponse
import com.ljwx.baseapp.util.BaseModuleLog

abstract class BaseDataRepository<Server> : IBaseDataRepository<Server> {

    companion object {

        private var globalObserverOnError: ((code: Int?, e: Throwable?) -> Unit)? = null

        private var globalResponseFail: ((code: Int?, message: String?) -> Unit)? = null

        /**
         * 通用的请求错误逻辑(代码执行错误)
         */
        fun setCommonOnError(onError: (code: Int?, e: Throwable?) -> Unit) {
            //包含了接口响应404
            globalObserverOnError = onError
        }

        /**
         * 通用的请求失败逻辑(接口返回结果失败)
         */
        fun setCommonResponseFail(responseFail: (code: Int?, message: String?) -> Unit) {
            globalResponseFail = responseFail
        }
    }

    open val TAG = this.javaClass.simpleName + BaseLogTag.DATA_REPOSITORY

    open val pageDataSize = 20
    protected var pageDataOffset = 0

    private var mCompositeDisposable2: io.reactivex.disposables.CompositeDisposable? = null
    private var mCompositeDisposable3: io.reactivex.rxjava3.disposables.CompositeDisposable? = null

    protected val mApiServer
        get() = createServer()

    open fun refreshOffset(refresh: Boolean) {
        pageDataOffset = if (refresh) 0 else pageDataOffset
    }

    open fun increaseOffset(list: List<Any?>?) {
        pageDataOffset += list?.size ?: 0
        BaseModuleLog.d(TAG, "当前offset值:$pageDataOffset")
    }


    /**
     * 页面销毁时,自动取消网络请求
     *
     * @param disposable Rxjava2版本的dispose
     */
    override fun autoClear(disposable: io.reactivex.disposables.Disposable) {
        BaseModuleLog.d(TAG, "添加Rx2自动取消")
        if (mCompositeDisposable2 == null) {
            mCompositeDisposable2 = io.reactivex.disposables.CompositeDisposable()
        }
        mCompositeDisposable2?.add(disposable)
    }

    /**
     * 页面销毁时,自动取消网络请求
     *
     * @param disposable Rxjava3版本的dispose
     */
    override fun autoClear(disposable: io.reactivex.rxjava3.disposables.Disposable) {
        BaseModuleLog.d(TAG, "添加Rx3自动取消")
        if (mCompositeDisposable3 == null) {
            mCompositeDisposable3 = io.reactivex.rxjava3.disposables.CompositeDisposable()
        }
        mCompositeDisposable3?.add(disposable)
    }

    /**
     * 自动取消
     */
    override fun onRxCleared() {
        BaseModuleLog.d(TAG, "执行Rx自动取消")
        mCompositeDisposable2?.clear()
        mCompositeDisposable3?.clear()
    }

    /**
     * RxJava3版本的结果监听
     */
    abstract inner class QuickObserver3<T : Any> : io.reactivex.rxjava3.core.Observer<T>,
        IQuickObserver<T> {
        override fun onSubscribe(d: io.reactivex.rxjava3.disposables.Disposable) {
            autoClear(d)
        }

        override fun onError(e: Throwable) {
            BaseModuleLog.d(TAG, "本次请求异常报错:" + e.message)
            onResponseError(null, null, e)
        }

        override fun onComplete() {
            BaseModuleLog.d(TAG, "本次请求完成")
        }

        override fun onNext(value: T) {
            onResponse(value)
        }

        /**
         * 接口结果响应
         *
         * @param response 结果
         */
        override fun onResponse(response: T) {
            BaseModuleLog.d(TAG, "接口返回response")
            if (response is BaseResponse<*>) {
                if (response.isSuccess()) {
                    BaseModuleLog.d(TAG, "接口返回response,成功")
                    onResponseSuccess(response)
                } else {
                    BaseModuleLog.d(TAG, "接口返回response,失败")
                    onResponseFail(response.code, response.msg)
                }
            }
        }

        /**
         * 接口数据成功
         *
         * @param response 成功的结果
         */
        abstract override fun onResponseSuccess(response: T)

        open fun onResponseFailCompat(code: String?, message: String?, data: Any? = null) {
            onResponseFail(code.toIntSafe(0), message, data)
        }

        /**
         * 接口数据失败
         *
         * @param response 失败的结果
         */
        override fun onResponseFail(code: Int?, message: String?, data: Any?) {
            BaseModuleLog.d(TAG, "请求失败,code:$code")
            onResponseFailGlobal(code, message)
        }

        override fun onResponseFailGlobal(code: Int?, message: String?) {
            globalResponseFail?.invoke(code, message)
        }

        override fun onResponseError(code: Int?, message: String?, e: Throwable?) {
            BaseModuleLog.d(TAG, "请求错误,code:$code")
            onResponseErrorGlobal(null, e)
        }

        override fun onResponseErrorGlobal(code: Int?, e: Throwable?) {
            globalObserverOnError?.invoke(code, e)
        }

    }

    /**
     * RxJava2版本的结果监听
     */
    abstract inner class QuickObserver<T : Any> : io.reactivex.Observer<T>, IQuickObserver<T> {
        override fun onSubscribe(d: io.reactivex.disposables.Disposable) {
            autoClear(d)
        }

        override fun onError(e: Throwable) {
            BaseModuleLog.d(TAG, "本次请求异常报错:" + e.message)
            onResponseError(null, null, e)
        }

        override fun onComplete() {
            BaseModuleLog.d(TAG, "本次请求完成")
        }

        override fun onNext(value: T) {
            onResponse(value)
        }

        /**
         * 接口结果响应
         *
         * @param response 结果
         */
        override fun onResponse(response: T) {
            BaseModuleLog.d(TAG, "接口返回response")
            if (response is BaseResponse<*>) {
                if (response.isSuccess()) {
                    BaseModuleLog.d(TAG, "接口返回response,结果为成功")
                    onResponseSuccess(response)
                } else {
                    BaseModuleLog.d(TAG, "接口返回response,结果为失败")
                    onResponseFail(response.code, response.msg)
                }
            }
        }

        /**
         * 接口数据成功
         *
         * @param response 成功的结果
         */
        abstract override fun onResponseSuccess(response: T)

        open fun onResponseFailCompat(code: String?, message: String?, data: Any? = null) {
            onResponseFail(code.toIntSafe(0), message, data)
        }

        /**
         * 接口数据失败
         *
         * @param dataResult 失败的结果
         */
        override fun onResponseFail(code: Int?, message: String?, data: Any?) {
            BaseModuleLog.d(TAG, "请求失败,code:$code")
            onResponseFailGlobal(code, message)
        }

        override fun onResponseFailGlobal(code: Int?, message: String?) {
            globalResponseFail?.invoke(code, message)
        }

        override fun onResponseError(code: Int?, message: String?, e: Throwable?) {
            BaseModuleLog.d(TAG, "请求错误,code:$code")
            onResponseErrorGlobal(code, e)
        }

        override fun onResponseErrorGlobal(code: Int?, e: Throwable?) {
            globalObserverOnError?.invoke(code, e)
        }

    }

    /**
     * RxJava2版本的结果监听,简单封装
     */
    abstract inner class ObserverResponse<T> : io.reactivex.Observer<T> {
        override fun onSubscribe(d: io.reactivex.disposables.Disposable) {
            autoClear(d)
        }

        override fun onError(e: Throwable) {
            BaseModuleLog.d(TAG, "请求异常报错:" + e.message)
            globalObserverOnError?.invoke(null, e)
        }

        override fun onComplete() {
            BaseModuleLog.d(TAG, "请求完成")
        }

        override fun onNext(value: T) {
            BaseModuleLog.d(TAG, "请求结果返回")
            onResponse(value)
        }

        /**
         * 接口结果响应
         *
         * @param response 结果
         */
        abstract fun onResponse(response: T)

    }

}