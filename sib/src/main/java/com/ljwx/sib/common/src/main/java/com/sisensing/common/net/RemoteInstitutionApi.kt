package com.sisensing.common.net

import com.ljwx.baseapp.response.BaseResponse
import com.sisensing.common.entity.personalcenter.RemoteInstitutionShareEntity
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RemoteInstitutionApi {

    /**
     * 待处理的共享记录
     */
    @GET("/healthcare/app/patient/share/todoList")
    fun getShareInstitutionTodo(): Observable<BaseResponse<List<RemoteInstitutionShareEntity>?>>

    /**
     * 数据共享处理
     */
    @POST("/healthcare/app/patient/shareHandle")
    fun handleShare(@Body body: RequestBody): Observable<BaseResponse<Any?>>

    /**
     * 共享记录
     */
    @GET("/healthcare/app/patient/shareRecord")
    fun getShareRecords(): Observable<BaseResponse<List<RemoteInstitutionShareEntity>?>>

    /**
     * 已共享的机构
     */
    @GET("/healthcare/app/patient/sharing")
    fun getShareInstitutions(): Observable<BaseResponse<List<RemoteInstitutionShareEntity>?>>

    /**
     * 解除共享
     */
    @GET("/healthcare/app/patient/unShare/{id}")
    fun removeShare(@Path("id") id: String): Observable<BaseResponse<Any?>>


}