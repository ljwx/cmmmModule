package com.ljwx.sib.base.src.main.java.com.sisensing.base;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.base
 * @Author: f.deng
 * @CreateDate: 2021/2/24 17:03
 * @Description:
 */
public interface IBaseView {


    void showLoading(String message);

    void showTip(String message);

    void showSuccess(String message);

    void showError(String message);

    void dismissDialog();

    void dealErrCode(int code);
}
