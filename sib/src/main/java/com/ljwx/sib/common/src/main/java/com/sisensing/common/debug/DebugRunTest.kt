package com.sisensing.common.debug

import com.ljwx.baseapp.debug.RunDebugProxy
import com.sisensing.common.utils.Log
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class DebugRunTest(type: Int) : RunDebugProxy(type) {

    override fun run() {
        super.run()

        Observable.create(object :ObservableOnSubscribe<Int>{
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(0/0)
                emitter.onNext(3)
            }
        }).compose(object :ObservableTransformer<Int, Int>{
            override fun apply(upstream: Observable<Int>): ObservableSource<Int> {
                return upstream.onErrorResumeNext(object :
                    io.reactivex.functions.Function<Throwable, Observable<Int>>{
                    override fun apply(t: Throwable): Observable<Int> {
                        return Observable.error(Throwable("errorrrrr"))
                    }
                })
            }
        })/*.onErrorResumeNext(object :ObservableSource<Int>{
            override fun subscribe(observer: Observer<in Int>) {

            }

        })*/.subscribe({
            Log.d("ljwx2", "回调:$it")
        }, {
            Log.d("ljwx2", "报错:"+it.message)
        }, {
            Log.d("ljwx2", "完成")
        }, {
            Log.d("ljwx2", "dispose")
        })

//        Observable.fromArray(arrayOf(1, 2, 0, 3)).map {
//            100
//        }.subscribe({
//            Log.d("ljwx2", "回调:$it")
//        }, {
//            Log.d("ljwx2", "报错")
//        }, {
//            Log.d("ljwx2", "完成")
//        }, {
//            Log.d("ljwx2", "dispose")
//        })
    }

}