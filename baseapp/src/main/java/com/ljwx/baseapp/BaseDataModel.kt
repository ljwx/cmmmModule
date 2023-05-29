package com.ljwx.baseapp

import com.blankj.utilcode.util.ActivityUtils
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseDataModel {

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

}