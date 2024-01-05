package com.sisensing.common.base;

import com.blankj.utilcode.util.ToastUtils;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.viewmodel
 * @Author: f.deng
 * @CreateDate: 2021/2/24 15:46
 * @Description:
 */
public abstract class ResponseListenerSimple<T> implements ResponseListener<T, Object>{


    @Override
    public void onFail(int code, String message, Object errorData) {
        ToastUtils.showLong(message);
    }

    @Override
    public void onError(String message) {

    }
}
