package com.ljwx.baseapp.extensions


import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.AutoDispose
import autodispose2.ObservableSubscribeProxy
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.schedulers.Schedulers


fun <T> Observable<T>.lifecycle(
    lifecycleOwner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
): ObservableSubscribeProxy<T> {
    return observeOn(AndroidSchedulers.mainThread()).onErrorResumeNext { throwable: Throwable ->

        return@onErrorResumeNext ObservableSource {
            Log.d("onErrorResumeNext:", throwable.localizedMessage)
        }

    }.to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}


fun <T> Observable<T>.lifecycle(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread()).onErrorResumeNext { throwable: Throwable ->

        return@onErrorResumeNext ObservableSource {
            Log.d("onErrorResumeNext:", throwable.localizedMessage)
        }

    }
}

fun <T> Observable<T>.switch(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}
