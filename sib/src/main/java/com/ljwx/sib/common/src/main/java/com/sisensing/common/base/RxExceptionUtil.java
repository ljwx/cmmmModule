package com.sisensing.common.base;

import android.net.ParseException;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * ProjectName: cgm_app_android
 * Package: com.sisensing.sijoy.net
 * Author: f.deng
 * CreateDate: 2020/12/22 14:08
 * Description:
 */
public class RxExceptionUtil {

    public static String exceptionHandler(Throwable e){
        String errorMsg = "Unknown error";
        if (e instanceof UnknownHostException) {
            errorMsg = "Network Unavailable";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "Request network timeout";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "Parsing error";
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "Server unavailable";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "Server unavailable";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "Server unavailable";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
