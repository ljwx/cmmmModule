package com.sisensing.common.base;


import com.blankj.utilcode.util.Utils;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.sisensing.base.BuildConfig;
import com.sisensing.common.net.Api;
import com.sisensing.http.interceptor.logging.Level;
import com.sisensing.http.interceptor.logging.LoggingInterceptor;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static final int DEFAULT_TIME_OUT = 15;
    private static final int DEFAULT_READ_TIME_OUT = 15;
    private static final int DEFAULT_WRITE_TIME_OUT = 15;
    private static Retrofit retrofit;

    private RetrofitUtils() {
        init();
    }

    private static class Inner {

        private static RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    public static RetrofitUtils getInstance() {
        return Inner.INSTANCE;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        retrofit = getRetrofitBuilder().build();
    }


    public <T> T getRequest(Class<T> clazz) {
        return retrofit.create(clazz);
    }


    private OkHttpClient.Builder getOkHttpClientBuilder() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return new OkHttpClient.Builder()
                .sslSocketFactory(ssfFactory, new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new ParamInterceptor())
                .addInterceptor(new ChuckerInterceptor(Utils.getApp()))
                .addInterceptor(new LoggingInterceptor.Builder()//构建者模式
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build());
    }


    private Retrofit.Builder getRetrofitBuilder() {
        OkHttpClient okHttpClient = getOkHttpClientBuilder().build();
        String debugUrl = Api.getCacheHost(Api.TEST_BASE_URL);
        String rubingLocal = "http://192.168.30.43:8088/";
        String baseUrl = BuildConfig.DEBUG ? debugUrl : Api.EU_BASE_URL;
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(baseUrl);
    }

    public static Map<String, RequestBody> getRequestBodyMap(Map<String, Object> resourceMap) {
        Map<String, RequestBody> params = new HashMap<>();
        Iterator iterator = resourceMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry.getValue() instanceof String) {
                //判断值如果是String的话就直接put进去
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), (String) entry.getValue());
                params.put((String) entry.getKey(), body);
            } else if (entry.getValue() instanceof File) {
                //判断当前值是单个文件的话就把key设置成服务端所要的参数子端名加文件名，具体格式可以看下面的
                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) entry.getValue());
                params.put((String) entry.getKey() + "\";filename=\"" + ((File) entry.getValue()).getName() + "", body);
            } else if (entry.getValue() instanceof File[]) {
                //判断当前的值是文件数组的话，要把一个个文件拆开再put进去
                File[] files = (File[]) entry.getValue();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), f);
                        params.put((String) entry.getKey() + "\";filename=\"" + f.getName() + "", body);
                    }
                }
            }
        }
        return params;
    }

}
