package com.sisensing.common.net;

import com.sisensing.common.entity.Device.RemoteGlucoseBean;
import com.sisensing.common.entity.Device.ReportBean;
import com.sisensing.common.entity.QueryDeviceEntity;
import com.sisensing.common.entity.personalcenter.SharerDeviceEntity;
import com.sisensing.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author y.xie
 * @date 2021/3/17 9:24
 * @desc 设备相关接口
 */
public interface DeviceApiService {
    /**
     * 新增设备信息
     * @param requestBody
     * @return /cgm/app/device
     */
    @POST("/cgm/app/device")
    Observable<BaseResponse<String,String>> addDeviceInfo(@Body RequestBody requestBody);

    /**
     * 修改设备
     * @param deviceId
     * @param requestBody
     * @return
     */
    @PUT("/cgm/app/device/{deviceId}")
    Observable<BaseResponse<Boolean,Object>> modifyDeviceInfo(@Path("deviceId") String deviceId,@Body RequestBody requestBody);

    /**
     * 根据设备Id获取报告
     * @param deivceId
     * @return
     */
    @GET("/cgm/app/report/{deivceId}/report")
    Observable<BaseResponse<ReportBean,Object>> getReportByDeviceId(@Path("deivceId") String deivceId);

    /**
     * 申请创建报告
     * @param deivceId
     * @return
     */
    @GET("/cgm/app/report/{deivceId}/report/create")
    Observable<BaseResponse<Object,Object>> applyReport(@Path("deivceId") String deivceId);

    /**
     * 根据报告ID查看报告
     * @param reportId
     * @return
     */
    @GET("/cgm/app/report/{reportId}/pdf")
    Observable<BaseResponse<String,Object>> getReportById(@Path("reportId") long reportId);

    /**
     * 查询当前账号中的所有设备列表
     * @param pageNum
     * @param pageSize
     * @param lastStartTime
     * @return
     */
    @GET("/cgm/app/device")
    Observable<com.ljwx.baseapp.response.BaseResponse<QueryDeviceEntity>> queryDevice(@Query("pageNum") int pageNum,@Query("pageSize")
            int pageSize,@Query("lastStartTime") long lastStartTime);

    /**
     * 获取设备连接码（门诊）
     * @return
     */
    @POST("/cgm/app/device/connection/code")
    Observable<BaseResponse<String,Object>> getOutpatientConnectCode(@Body RequestBody body);

    /**
     * 根据开始时间和结束时间查询血糖数据
     */
    @GET("/cgm/app/device/glucoseData")
    Observable<BaseResponse<List<RemoteGlucoseBean>, Object>> getRemoteGlucoseData(
            @Query("startDate")String startDate, @Query("endDate")String endDate, @Query("dataSize") Integer dataSize);

    /**
     * 更新设备恢复状态
     * @param requestBody
     * @return
     */
    @PUT("/cgm/app/device/restore/status")
    Observable<BaseResponse<Object,Object>> updateDeviceRestoreStatus(@Body RequestBody requestBody);

    /**
     * 更新设备重启次数
     * @param requestBody
     * @return
     */
    @PUT("/cgm/app/device/restart/count")
    Observable<BaseResponse<Object,Object>> updateDeviceRestartCount(@Body RequestBody requestBody);

    /**
     * 设备扩展信息更新
     * @param requestBody
     * @return
     */
    @PUT("/cgm/app/device/extension")
    Observable<BaseResponse<Object,Object>> updateDeviceExpandInfo(@Body RequestBody requestBody);

    @POST("/cgm/app/device/glucoseDataList")
    Observable<com.ljwx.baseapp.response.BaseResponse<List<SharerDeviceEntity.GlucoseInfo>>> getDeviceGlucoseData(@Body RequestBody requestBody);

    @POST("/cgm/app/device/glucoseDataList/export")
    Observable<ResponseBody> getDeviceGlucoseExcel(@Body RequestBody requestBody);

}
