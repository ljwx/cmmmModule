package com.sisensing.common.download;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.download
 * @Author: l.chenlu
 * @CreateDate: 2021/6/16 20:16
 * @Description:
 */
public interface OnDownloadListener {

    void onStart();

    void onProgrss(int p);

    void onFinish();

    void onException(Throwable e);
}
