package com.sisensing.common.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.net
 * @Author: l.chenlu
 * @CreateDate: 2021/6/16 16:53
 * @Description:
 */
public interface DownloadApiService {

    @Streaming
    @GET
    Observable<ResponseBody> downloadFileByUrl(@Url String url);
}
