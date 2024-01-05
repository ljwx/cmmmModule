package com.sisensing.common.net

import com.ljwx.baseapp.response.BaseResponse
import com.sisensing.common.entity.alarm.UserGlucoseAlarmSettingsData
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface GlucoseAlarmApiService {

    /**
     * 获取报警信息
     */
    @GET("/user/app/v2/alarmInfo")
    fun userGlucoseAlarmSettings(): Observable<BaseResponse<UserGlucoseAlarmSettingsData?>>

    /**
     * 更新报警信息
     */
    @PUT("/user/app/v2/alarmInfo")
    fun updateGlucoseAlarmSettings(@Body body: UserGlucoseAlarmSettingsData): Observable<BaseResponse<Any?>>

    /**
     * 保存分享的好友报警信息
     */
    @POST("/user/app/follow/v2/alertSetting")
    fun saveRemoteAlertSetting(@Body body: RequestBody): Observable<BaseResponse<Any?>>

    /**
     *
     */
    @POST("/user/app/alarm/know")
    fun updateAlarmKnow(@Body body: RequestBody): Observable<BaseResponse<Any?>>
}