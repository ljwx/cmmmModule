package com.sisensing.common.base;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.base.BaseMvvmActivity;
import com.sisensing.base.BaseMvvmFragment;
import com.sisensing.base.BaseViewModel;
import com.sisensing.common.R;
import com.sisensing.common.user.UserInfoUtils;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.base
 * @Author: f.deng
 * @CreateDate: 2021/2/24 17:50
 * @Description:
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseMvvmFragment<V, VM> {

    @Override
    public void showTip(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.showTip(message);
        }
    }

    @Override
    public void showLoading(String message) {

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.showLoading(message);
        }
    }

    @Override
    public void showSuccess(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.showSuccess(message);
        }
    }

    @Override
    public void showError(String message) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.showError(message);
        }
    }


    @Override
    public void dismissDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.dismissDialog();
        }
    }

    @Override
    public void dealErrCode(int code) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity instanceof BaseMvvmActivity) {
            activity.dealErrCode(code);
        }
    }
}
