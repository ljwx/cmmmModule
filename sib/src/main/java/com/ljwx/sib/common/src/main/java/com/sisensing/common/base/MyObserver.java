package com.sisensing.common.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.sisensing.http.BaseResponse;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * ProjectName: cgm_app_android
 * Package: com.sisensing.sijoy.net
 * Author: f.deng
 * CreateDate: 2020/12/22 14:05
 * Description:
 */

public abstract class MyObserver<T extends BaseResponse> implements Observer<T> {

    private boolean mShowDialog;
    private PromptDialog mPromptDialog;
    private Disposable d;
    protected Activity mContext;

    public MyObserver(Activity context, Boolean showDialog) {
        this.mContext = context;
        mShowDialog = showDialog;
    }

    public MyObserver(Activity context) {
        this(context, true);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        } else {
            try {
                if (mPromptDialog == null && mShowDialog == true) {
                    mPromptDialog = new PromptDialog(mContext);
                    mPromptDialog.showLoading("");
                }
            } catch (Exception e) {
                cancelRequest();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        onFailure(e, RxExceptionUtil.exceptionHandler(e));
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
    }


    public void hidDialog() {
        if (mPromptDialog != null && mShowDialog == true) {
            mPromptDialog.dismiss();
            mPromptDialog = null;
        }
    }


    public abstract void onSuccess(T data);

    public abstract void onFailure(Throwable e, String errorMsg);

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        boolean available = info.isAvailable();
        return available;
    }

    /**
     * 取消订阅
     */
    public void cancelRequest() {
        if (d != null && d.isDisposed()) {
            d.dispose();
            hidDialog();
        }
    }

}
