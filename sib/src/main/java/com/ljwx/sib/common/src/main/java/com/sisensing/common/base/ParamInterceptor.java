package com.sisensing.common.base;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.user.UserInfoUtils;

import java.io.IOException;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ProjectName: cgm_app_android
 * Package: com.sisensing.sijoy.net
 * Author: f.deng
 * CreateDate: 2020/12/22 14:26
 * Description:
 */
public class ParamInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = UserInfoUtils.getToken();

        Request.Builder builder = chain.request().newBuilder();

        if (ObjectUtils.isNotEmpty(token)) {
            builder.addHeader("Authorization", UserInfoUtils.getToken()).build();
        }
        builder.addHeader("lang", Constant.CURRENT_LANGUAGE);
        builder.addHeader("timeZone", TimeZone.getDefault().getID());
        return chain.proceed(builder.build());
    }
}
