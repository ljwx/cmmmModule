package com.sisensing.common.net;

import com.sisensing.common.entity.personalcenter.AppUpdateEntity;
import com.sisensing.common.entity.personalcenter.DictTypeEntity;
import com.sisensing.common.entity.personalcenter.DoctorDetailEntity;
import com.sisensing.common.entity.personalcenter.FollowerEntity;
import com.sisensing.common.entity.personalcenter.FollowerTodoEntity;
import com.sisensing.common.entity.personalcenter.LifeEventEntity;
import com.sisensing.common.entity.personalcenter.MyFollowListEntity;
import com.sisensing.common.entity.personalcenter.PersonalInfoEntity;
import com.sisensing.common.entity.personalcenter.ReportListEntity;
import com.sisensing.common.entity.personalcenter.SharerDeviceEntity;
import com.sisensing.common.entity.personalcenter.SharerEntity;
import com.sisensing.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author y.xie
 * @date 2021/5/17 15:39
 * @desc
 */
public interface PersonalCenterApiService {
    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("/user/app/info")
    Observable<BaseResponse<PersonalInfoEntity, Object>> getUserInfo();

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/user/app/avatar/upload")
    Observable<BaseResponse<Object, Object>> uploadAvatar(@Part MultipartBody.Part file);

    /**
     * 更新用户信息
     *
     * @param body
     * @return
     */
    @PUT("/user/app/info")
    Observable<BaseResponse<Object, Object>> updateUseInfo(@Body RequestBody body);

    /**
     * 报警信息更新
     *
     * @param body
     * @return
     */
    @PUT("/user/app/alarmInfo")
    Observable<BaseResponse<Object, Object>> updateAlarmInfo(@Body RequestBody body);

    /**
     * 获取用户行为数据
     *
     * @return
     */
    @GET("/user/app/action")
    Observable<BaseResponse<LifeEventEntity, Object>> getAction(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize,
                                                                @Query("startActionTime") Long start, @Query("endActionTime") Long end);

    /**
     * 检查APP版本更新
     *
     * @param appCode
     * @param platform
     * @param version
     * @return
     */
    @GET("/system/app/appManager/app/check")
    Observable<BaseResponse<AppUpdateEntity, Object>> checkAppUpdate(@Query("appCode") String appCode, @Query("platform") int platform, @Query("version") int version);

    /**
     * 根据设备ID获取医生信息
     *
     * @param
     * @return
     */
    @GET("/medicine/miniprogram/sign/own/signDoctor")
    Observable<BaseResponse<DoctorDetailEntity, Object>> getDoctor();

    /**
     * 绑定医生
     *
     * @param deviceId
     * @param body
     * @return
     */
    @PUT("/cgm/app/device/{deviceId}/signDoctor")
    Observable<BaseResponse<Object, Object>> bindDoctor(@Path("deviceId") String deviceId, @Body RequestBody body);

    /**
     * 查询我的关注列表信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("/follow/app/list")
    Observable<BaseResponse<MyFollowListEntity, Object>> getMyFollow(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 新增关注信息
     *
     * @param body
     * @return
     */
    @POST("/follow/app")
    Observable<BaseResponse<Object, Object>> addFollow(@Body RequestBody body);

    /**
     * 查询关注我的列表信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("/follow/app/followed/list")
    Observable<BaseResponse<MyFollowListEntity, Object>> getFollowMe(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 更新关注信息
     *
     * @param followId
     * @param body
     * @return
     */
    @PUT("/follow/app/{followId}")
    Observable<BaseResponse<Object, Object>> updateFollow(@Path("followId") String followId, @Body RequestBody body);

    /**
     * 根据userId获取手机号
     *
     * @param phoneNumber
     * @return
     */
    @GET("/user/app/user/{phoneNumber}")
    Observable<BaseResponse<String, Object>> getUserIdByPhoneNumber(@Path("phoneNumber") String phoneNumber);

    /**
     * 获取分享图片
     *
     * @return
     */
    @GET("/user/miniprogram/sign/user/follow/share/qrcode")
    Observable<BaseResponse<String, Object>> shareQrcode();

    /**
     * 根据用户ID获取报告列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GET("/cgm/app/report/{userId}/list")
    Observable<BaseResponse<ReportListEntity, Object>> getReportListByUserId(@Path("userId") String userId, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * @param dictType 类型: dr_type：病人类型 complications：并发症  treatment_method：治疗方式
     * @return
     */
    @GET("/system/dict/data/type/{dictType}")
    Observable<BaseResponse<List<DictTypeEntity>, Object>> getDictTypeList(@Path("dictType") String dictType);

    /**
     * 停止签约医生
     *
     * @param doctorId
     * @return
     */
    @GET("/medicine/miniprogram/sign/doctor/{doctorId}/cancel")
    Observable<BaseResponse<Boolean, Object>> stopSignDoctor(@Path("doctorId") String doctorId);

    /**
     * 授权用户信息绑定门诊
     */
    @POST("/user/app/approve/own/info")
    Observable<BaseResponse<Boolean, Object>> auUserInfoBind(@Body RequestBody body);

    /**
     * 关注者列表查询(我关注的人列表)
     *
     * @return
     */
    @GET("/user/app/follow/sharer")
    Observable<BaseResponse<List<SharerEntity>, Object>> getSharerList();

    /**
     * 被关注者列表查询(关注我的人列表)
     *
     * @return
     */
    @GET("/user/app/follow/follower")
    Observable<BaseResponse<List<FollowerEntity>, Object>> getFollowerList();

    /**
     * 血糖分享邀请
     *
     * @return
     */
    @POST("/user/app/follow/invite")
    Observable<BaseResponse<String, Object>> invite(@Body RequestBody body);

    /**
     * 血糖分享邀请处理
     *
     * @return
     */
    @POST("/user/app/follow/invite/handle")
    Observable<BaseResponse<String, Object>> inviteHandle(@Body RequestBody body);

    /**
     * 血糖分享报警设置
     *
     * @return
     */
    @POST("/user/app/follow/alertSetting")
    Observable<BaseResponse<Object, Object>> alertSetting(@Body RequestBody body);

    /**
     * 解除关注
     *
     * @return
     */
    @POST("/user/app/follow/unfollow")
    Observable<BaseResponse<String, Object>> unfollow(@Body RequestBody body);

    /**
     * 待处理邀请列表 (用于强处理)
     *
     * @return
     */
    @POST("/user/app/follow/todoList")
    Observable<BaseResponse<List<FollowerTodoEntity>, Object>> followTodoList();

    /**
     * 被关注人最新设备血糖
     *
     * @return
     */
    @POST("/user/app/follow/deviceGlucose")
    Observable<BaseResponse<SharerDeviceEntity, Object>> followDeviceGlucose(@Body RequestBody body);

    /**
     * 注销账号
     *
     * @return
     */
    @POST("/user/app/deleteAccount")
    Observable<BaseResponse<String, Object>> deleteAccount(@Body RequestBody body);


    /**
     * 保存用户的语言
     *
     * @return
     */
    @POST("/user/app/language/save")
    Observable<BaseResponse<String, Object>> saveLanguage(@Body RequestBody body);

    /**
     * 保存用户血糖单位
     *
     * @return
     */
    @POST("/user/app/glucoseUnit/save")
    Observable<BaseResponse<Object,Object>> saveBGUnit(@Body RequestBody body);
}
