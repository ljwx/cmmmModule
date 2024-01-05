package com.ljwx.basenetwork.retrofit.test

import com.ljwx.baseapp.response.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TestService {

    /**
     * https://search.gitee.com/?skin=rec&type=repository&q=text
     */
    @GET("/api/api-mima/mima.php")
    open fun search1(
        @Query("msg") msg: String = "10"
    ): Call<String>

    @GET("/api/api-mima/mima.php")
    open fun search2(
        @Query("msg") msg: String = "10"
    ): Observable<String>

    @GET("/api/api-mima/mima.php")
    open fun search3(
        @Query("msg") msg: String = "10"
    ): Call<BaseResponse<String>>
}