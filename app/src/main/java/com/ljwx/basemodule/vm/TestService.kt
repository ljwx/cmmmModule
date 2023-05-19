package com.ljwx.basemodule.vm

import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface TestService {

    /**
     * https://search.gitee.com/?skin=rec&type=repository&q=text
     */
    @GET("/")
    open fun search1(
        @Query("q") q: String?,
        @Query("type") type: String? = "repository",
        @Query("skin") skin: String? = "rec"
    ): Call<String>

    @GET("/")
    open fun search2(
        @Query("q") q: String?,
        @Query("type") type: String? = "repository",
        @Query("skin") skin: String? = "rec"
    ): Observable<String>
}