package com.ljwx.basemodule.vm

import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.vm.BaseDataRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestRepository : BaseDataRepository() {

    fun requestTest(): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        mGiteeTestRetrofit.create(TestService::class.java).search1("text")
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        liveData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })
        return liveData
    }

    fun requestTest2() {
        mGiteeTestRetrofit.create(TestService::class.java).search2("text")
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }

                override fun onComplete() {

                }

                override fun onNext(t: String) {

                }

            })
    }

}