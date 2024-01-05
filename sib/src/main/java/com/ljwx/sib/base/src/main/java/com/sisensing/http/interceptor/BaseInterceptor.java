package com.ljwx.sib.base.src.main.java.com.sisensing.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class BaseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        /*Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }
        //请求信息
        return chain.proceed(builder.build());*/


      //  String token = UserInfoUtils.getToken();

        Request.Builder builder = chain.request().newBuilder();

        /*if (UserInfoUtils.isLogin()) {
            if (ObjectUtils.isNotEmpty(token)) {
                builder.addHeader("Authorization", UserInfoUtils.getToken())
                        .build();
            }
        }*/

        return chain.proceed(builder.build());
    }
}