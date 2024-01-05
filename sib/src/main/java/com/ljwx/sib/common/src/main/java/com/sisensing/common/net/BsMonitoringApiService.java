package com.sisensing.common.net;

import com.sisensing.common.entity.BloodGlucoseEntity.RemoteDataBean;
import com.sisensing.common.entity.NewlyAddedBsDataEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.drug.DrugLibEntity;
import com.sisensing.common.entity.personalcenter.LifeEventEntity;
import com.sisensing.http.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author y.xie
 * @date 2021/3/5 17:06
 * @desc
 */
public interface BsMonitoringApiService {
    /**
     * 获取设备当前数据index
     *
     * @param deviceId
     * @return
     */
    @GET("/cgm/app/device/{deviceId}/fullIndexInfo")
    Observable<BaseResponse<RemoteDataBean, Object>> getCurrentIndex(@Path("deviceId") String deviceId);

    /**
     * 上传血糖数据(新增设备原始数据)
     *
     * @return
     */
    @POST("/cgm/app/device/{deviceId}/data")
    Observable<BaseResponse<Boolean, Object>> upBsDataOld(@Path("deviceId") String deviceId, @Body RequestBody body);

    /**
     * 上传血糖数据(新增设备原始数据)
     *
     * @return
     */
    @POST("/cgm/app/device/v2/{deviceId}/data")
    Observable<BaseResponse<NewlyAddedBsDataEntity, Object>> upBsData(@Path("deviceId") String deviceId, @Body RequestBody body);

    /**
     * 用户行为数据新增
     *
     * @return
     */
    @POST("/user/app/addAction")
    Observable<BaseResponse<String, Object>> addActionEvent(@Body RequestBody body);

    /**
     * 用户行为数据新增
     *
     * @return
     */
    @Multipart
    @POST("/user/app/{userId}/action")
    Observable<BaseResponse<String, Object>> addActionEvent(@Path("userId") String userId, @PartMap Map<String, RequestBody> params);

    /**
     *药物信息查询
     * @param categoryName
     * @param pageNum 当前页
     * @param pageSize 每页条数
     * @param drugName 药物名称
     * @return
     */
    @GET("/medicine/app/drug")
    Observable<BaseResponse<DrugLibEntity,Object>> searchDrugs(@Query("categoryName") String categoryName,@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("drugName") String drugName);

    @POST("/user/app/addAction")
//    /app/action/batch
    Observable<BaseResponse<String, Object>> batchAddActionEvent(@Body RequestBody body);

    /**
     *打卡数据批量上传接口
     * @param userId
     * @param body
     * @return
     */
    @POST("/user/app/{userId}/action/batch")
    Observable<BaseResponse<List<String>, Object>> batchAddActionEvent(@Path("userId") String userId,@Body RequestBody body);

    @Multipart
    @POST("/user/app/file/upload/image")
    Observable<BaseResponse<List<String>,Object>> uploadFile(@PartMap Map<String, RequestBody> files);

    @DELETE("/user/app/action")
    Observable<BaseResponse<Object, Object>> deleteCheckIn(@Query("actionId") String actionId);

    @GET("/user/app/user/action/detail")
    Observable<BaseResponse<LifeEventEntity.RecordsDTO, Object>> getCheckInData(@Query("actionId") String actionId);

    @POST("/user/app/updateAction")
    Observable<BaseResponse<String, Object>> updateCheckIn(@Body RequestBody body);

}
