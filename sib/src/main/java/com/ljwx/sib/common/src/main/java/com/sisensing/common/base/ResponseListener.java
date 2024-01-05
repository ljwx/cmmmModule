package com.sisensing.common.base;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.viewmodel
 * @Author: f.deng
 * @CreateDate: 2021/2/24 15:46
 * @Description:
 */
public interface ResponseListener<T, U> {


    void onSuccess(T data,String msg);


    void onFail(int code, String message, U errorData);


    void onError(String message);

}
