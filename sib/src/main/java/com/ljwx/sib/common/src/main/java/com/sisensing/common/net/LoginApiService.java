package com.sisensing.common.net;

import com.sisensing.common.entity.login.CountryBean;
import com.sisensing.common.entity.login.LoginBean;
import com.sisensing.common.entity.login.ModifyPwdResponseBean;
import com.sisensing.common.entity.login.UserInfoBean;
import com.sisensing.common.entity.privacy.CheckUserPrivacyEntity;
import com.sisensing.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author y.xie
 * @date 2021/3/4 15:12
 * @desc 登陆相关
 */
public interface LoginApiService {
    /**
     * 注册短信验证码
     *
     * @param phoneNumber 手机号
     * @return
     */
    @GET("user/app/{phoneNumber}/smsCode/{type}")
    Observable<BaseResponse<String,Object>> getRegisterSmsCode(@Path("phoneNumber") String phoneNumber, @Path("type") String type);

    /**
     * 校验手机号码是否注册过
     * @param phoneNumber
     * @return
     */
    @GET("user/app/register/{phoneNumber}")
    Observable<BaseResponse<Object,Object>> phoneIsRegister(@Path("phoneNumber") String phoneNumber);
    /**
     * 用户信息注册
     *
     * @return
     */
    @POST("/user/app/register")
    Observable<BaseResponse<String,Object>> register(@Body RequestBody body);

    /**
     * 用户登录
     *
     * @return
     */
    @POST("/auth/app/user/login")
    Observable<BaseResponse<LoginBean,Object>> login(@Body RequestBody body);

    @POST("/auth/app/user/login")
    Observable<com.ljwx.baseapp.response.BaseResponse<LoginBean>> loginBase(@Body RequestBody body);

    /**
     * 登录信息
     *
     * @param token 用户的token
     * @return
     */
    @GET("/auth/token/info")
    Observable<BaseResponse<UserInfoBean,Object>> getUserInfo(@Header("Authorization") String token);

    /**
     * 修改/重置密码
     *
     * @param body
     * @return
     */
    @PUT("/user/app/resetPassword")
    Observable<BaseResponse<ModifyPwdResponseBean,Object>> modifyOrResetPwd(@Body RequestBody body);

    /**
     * 微信登录
     * @param code
     * @return
     */
    @GET("/auth/app/user/weChatLogin")
    Observable<BaseResponse<LoginBean,Object>> weChatLogin(@Query("code") String code);

    /**
     * 微信注册
     * @param body
     * @return
     */
    @POST("user/app/weChatRegist")
    Observable<BaseResponse<LoginBean,Object>> weChatRegister(@Body RequestBody body);

    /**
     * 设置密码
     * @param body
     * @return
     */
    @PUT("/user/app/password")
    Observable<BaseResponse<Object,Object>> setPassword(@Body RequestBody body);

    /**
     * 终端设备信息上传
     * @param body
     * @return
     */
    @POST("/user/app/terminal/info")
    Observable<BaseResponse<Object,Object>> uploadTerminalInfo(@Body RequestBody body);

    /**
     *终端设备连接信息上传
     */
    @POST("/cgm/app/device/connect/info")
    Observable<BaseResponse<Object,Object>> uploadConnectInfo(@Body RequestBody body);

    /**
     * 重置密码验证码
     *
     * @return
     */
    @POST("/user/email/send/code")
    Observable<BaseResponse<String,Object>> getResetPwdSmsCode(@Body RequestBody body);

    /**
     * 获取国家列表
     *
     * @return
     */
    @GET("/user/app/country/list")
    Observable<BaseResponse<List<CountryBean>,Object>> getCountryList();

    /**
     * 获取用户当前选择的国家
     *
     * @return
     */
    @GET("/user/app/country")
    Observable<BaseResponse<CountryBean,Object>> userCountry();

    /**
     * 用户选择国家
     *
     * @return
     */
    @PUT("/user/app/country")
    Observable<BaseResponse<CountryBean,Object>> setCountry(@Body RequestBody body);

    /**
     * 保存fcm token
     *
     * @return
     */
    @POST("/user/app/fcmToken/save")
    Observable<BaseResponse<String,Object>> saveFcmToken(@Body RequestBody body);

    @POST("/user/app/policy/checkUpdate")
    Observable<BaseResponse<CheckUserPrivacyEntity,Object>> checkPrivacyVersion(@Body RequestBody body);

    @POST("/user/app/policy/update")
    Observable<BaseResponse<Object,Object>> updatePrivacy(@Body RequestBody body);
}
