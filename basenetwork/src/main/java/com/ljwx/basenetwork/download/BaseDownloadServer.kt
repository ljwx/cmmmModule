package com.ljwx.basenetwork.download

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface BaseDownloadServer {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>

}