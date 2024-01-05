package com.sisensing.common.net

import com.sisensing.common.entity.Device.AGPReportApplyBean
import com.sisensing.http.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url


interface AGPReportApiService {

    @POST("/report/app/report/apply")
    fun applyAGPReport(@Body requestBody: RequestBody?): Observable<BaseResponse<AGPReportApplyBean?, Any>?>

    @POST("/report/app/report/sendEmail")
    fun sendEmail(@Body requestBody: RequestBody?): Observable<BaseResponse<Any?, Any>?>

    @GET("/report/app/report/taskResult/{taskId}")
    fun getApplyResult(@Path("taskId") taskId: Long): Observable<BaseResponse<AGPReportApplyBean?, Any>?>

}