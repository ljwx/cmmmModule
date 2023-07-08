package com.ljwx.basenetwork.retrofit.test

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.ljwx.baseapp.vm.BaseDataRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TestRepository : BaseDataRepository() {

    val mGiteeTestRetrofit by lazy {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(ActivityUtils.getTopActivity()))
            .build()
        Retrofit.Builder()
            .baseUrl("https://gitee.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapter())
            .build()
    }

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