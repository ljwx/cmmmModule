package com.ljwx.basenetwork.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtils {

    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        return retrofit ?: initRetrofit()
    }

    private fun initOkhttp(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }

    private fun initRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .client(initOkhttp().build())
            .baseUrl("https://www.baidu.com") //addConverterFactory解析有先后顺序，按需添加设置
            .addConverterFactory(GsonConverterFactory.create()) //转换为Gson对象
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //rxJava集成
            .build()
        return retrofit!!
    }

}