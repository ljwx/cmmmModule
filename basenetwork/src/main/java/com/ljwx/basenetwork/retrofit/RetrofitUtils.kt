package com.ljwx.basenetwork.retrofit

import retrofit2.Retrofit

object RetrofitUtils {

    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        return retrofit!!
    }

}