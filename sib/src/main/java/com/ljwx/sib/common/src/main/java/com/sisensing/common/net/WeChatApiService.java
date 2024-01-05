package com.sisensing.common.net;

import com.sisensing.common.entity.wechat.WeChatAccessTokenBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author y.xie
 * @date 2021/3/23 13:48
 * @desc 微信相关接口
 */
public interface WeChatApiService {
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<WeChatAccessTokenBean> getAccessToken(
            @Query("appid") String appid,
            @Query("secret") String secret,
            @Query("code") String code,
            @Query("grant_type") String grant_type
    );
}
