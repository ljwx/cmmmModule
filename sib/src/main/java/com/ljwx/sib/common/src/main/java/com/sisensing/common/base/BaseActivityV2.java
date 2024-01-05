package com.sisensing.common.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.base.BaseMvvmActivity;
import com.sisensing.base.BaseMvvmActivityV2;
import com.sisensing.base.BaseViewModel;
import com.sisensing.base.IBaseView;
import com.sisensing.common.R;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.AppLanguageUtils;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.base
 * @Author: f.deng
 * @CreateDate: 2021/2/24 16:41
 * @Description:
 */
public abstract class BaseActivityV2<V extends ViewDataBinding, VM extends BaseViewModel<?>> extends BaseMvvmActivityV2<V, VM> implements IBaseView {


    public PromptDialog mPromptDialog;

//    private FragmentTransaction fragmentTransaction = null;


    @Override
    public void showTip(String message) {

        ToastUtils.getDefaultMaker()
                .setGravity(Gravity.CENTER, 0, 0)
                .setBgResource(R.drawable.common_bg_tip)
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .showShort(message);
    }

    @Override
    public void showLoading(String message) {
        if (mPromptDialog == null) {
            mPromptDialog = new PromptDialog(this);
        }
        mPromptDialog.showLoading(message);

    }

    @Override
    public void showSuccess(String message) {
        if (mPromptDialog == null) {
            mPromptDialog = new PromptDialog(this);
        }
        mPromptDialog.showSuccess(message);
    }

    @Override
    public void showError(String message) {
        if (mPromptDialog == null) {
            mPromptDialog = new PromptDialog(this);
        }
        mPromptDialog.showError(message);
    }

    @Override
    public void dismissDialog() {
        if (mPromptDialog != null) {
            mPromptDialog.dismiss();
        }
    }

    @Override
    public void dealErrCode(int code) {
        if (code == 401) {
            //登录过期
            ToastUtils.showShort(getString(R.string.common_be_overdue_log_in_again));
            UserInfoUtils.loginOut();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLanguageUtils.activityLanguage(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPromptDialog = null;
    }


    @Override
    public void init() {
        super.init();

        //沉浸式状态栏
        BarUtils.transparentStatusBar(this);
        BarUtils.setStatusBarLightMode(this, true);

    }

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


//    /**
//     * showFragment
//     *
//     * @param layout
//     * @param tag
//     */
//    public void showFragment(int layout, String tag) {
//        Fragment f = getFragment(tag);
//        if (f != null) {
//            ensureTransaction();
//            if (!f.isAdded()) {
//                fragmentTransaction.add(layout, f, tag);
//            }
//            fragmentTransaction.show(f);
//            fragmentTransaction.setMaxLifecycle(f, Lifecycle.State.RESUMED);
//
//        }
//        commitTransactions(tag);
//    }
//
//    /**
//     * 隐藏fragment
//     *
//     * @param tag
//     */
//    public void hideFragment(String tag) {
//        Fragment f = getFragment(tag);
//        if (f != null && !f.isDetached()) {
//            ensureTransaction();
//            fragmentTransaction.hide(f);
//            fragmentTransaction.setMaxLifecycle(f, Lifecycle.State.STARTED);
//        }
//    }
//
//    public Fragment getFragment(String tag) {
//        Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
//        if (f == null) {
//            f = (Fragment) ARouter.getInstance().build(tag).navigation();
//        }
//        return f;
//    }
//
//    public void commitTransactions(String tag) {
//        if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
//            fragmentTransaction.commitAllowingStateLoss();
//            fragmentTransaction = null;
//        }
//    }
//
//    private FragmentTransaction ensureTransaction() {
//        if (fragmentTransaction == null) {
//            fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        }
//        return fragmentTransaction;
//
//    }


    /**
     * 重写 getResource 方法，防止系统字体影响
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration newConfig = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (resources != null && newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
            } else {
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
    }


}
