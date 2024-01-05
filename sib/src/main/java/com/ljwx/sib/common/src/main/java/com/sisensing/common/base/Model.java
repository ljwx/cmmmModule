package com.sisensing.common.base;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.base.BaseModel;
import com.sisensing.base.BaseViewModel;
import com.sisensing.common.R;
import com.sisensing.common.utils.RxUtils;
import com.sisensing.http.BaseResponse;
import com.sisensing.http.ResponseThrowable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.viewmodel
 * @Author: f.deng
 * @CreateDate: 2021/2/24 15:04
 * @Description:
 */
public class Model extends BaseModel<Object> {


    private BaseViewModel mViewModel;

    public Model(BaseViewModel viewModel) {
        super();
        this.mViewModel = viewModel;
    }

    public Model() {
    }

    /**
     * 将参数封装成RequestBody
     *
     * @param obj 请求参数javaBean类型
     * @return requestBody
     */
    public RequestBody getRequestBody(Object obj) {
        String json = GsonUtils.toJson(obj);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        return requestBody;
    }

    /**
     *  将字符串封装成RequestBody
     * @param str 字符串
     * @return
     */
    public RequestBody getRequestBody(String str) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), str);
        return requestBody;
    }

    /**
     * 发起网络请求
     *
     * @param observable
     * @param <T>
     */
    public <T, U> void requestFromServer(Observable<BaseResponse<T, U>> observable, final boolean showLoading, final ResponseListener<T, U> listener) {

        observable.compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())// 网络错误的异常转换
                .subscribe(new Observer<BaseResponse<T, U>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        if (NetworkUtils.isConnected()) {
                            if (showLoading) {
                                mViewModel.getUC().getShowLoadingEvent().postValue(mViewModel.getApplication().getString(R.string.common_loading));
                            }
                            addSubscribe(d);
                        } else {
                            if (listener!=null){
                                listener.onError(StringUtils.getString(R.string.network_abnormal_please_try_again_later));
                            }
                            d.dispose();
                            if (mViewModel == null) return;
                            mViewModel.getUC().getShowErrorEvent().postValue(mViewModel.getApplication().getString(R.string.network_not_connected));
                        }

                    }

                    @Override
                    public void onNext(BaseResponse<T, U> response) {
                        if (showLoading) {
                            if (ObjectUtils.isNotEmpty(response.getMsg())) {
                                mViewModel.getUC().getDismissDialogEvent().call();
                            }
                        }
                        //请求成功
                        if (response.isOk()) {
                            if (listener!=null) {
                                listener.onSuccess(response.getData(),response.getMsg());
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            if (mViewModel!=null){
                                mViewModel.getUC().getErrCodeEvent().postValue(response.getCode());
                            }
                            if (listener!=null){
                                listener.onFail(response.getCode(), response.getMsg(), response.getErrorData());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("错误",e.getMessage());
                        if (e instanceof ResponseThrowable) {
                            if (mViewModel != null) {
                                //((ResponseThrowable) e).message
                                mViewModel.getUC().getShowErrorEvent().postValue(StringUtils.getString(R.string.network_abnormal_please_try_again_later));
                            }

                            if (listener!=null){
                                listener.onError(StringUtils.getString(R.string.network_abnormal_please_try_again_later));
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public <T, U> void requestFromServer(Observable<BaseResponse<T, U>> observable, final ResponseListener<T, U> listener) {

        this.requestFromServer(observable, true, listener);

    }

    /**
     * 发起网络请求
     *
     * @param observable
     * @param <T>
     */
    public <T> void requestFromServerNew(Observable<com.ljwx.baseapp.response.BaseResponse<T>> observable, final boolean showLoading, final ResponseListener<T, Object> listener) {
        observable.compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())// 网络错误的异常转换
                .subscribe(new Observer<com.ljwx.baseapp.response.BaseResponse<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (NetworkUtils.isConnected()) {
                            if (showLoading) {
                                mViewModel.getUC().getShowLoadingEvent().postValue(mViewModel.getApplication().getString(R.string.common_loading));
                            }
                            addSubscribe(d);
                        } else {
                            if (mViewModel != null) {
                                mViewModel.getUC().getShowErrorEvent().postValue(mViewModel.getApplication().getString(R.string.network_not_connected));
                                return;
                            }
                            if (listener!=null){
                                listener.onError("");
                            }
                            d.dispose();
                        }

                    }

                    @Override
                    public void onNext(com.ljwx.baseapp.response.BaseResponse<T> response) {
                        if (showLoading) {
                            if (ObjectUtils.isNotEmpty(response.getMsg())) {
                                mViewModel.getUC().getDismissDialogEvent().call();
                            }
                        }
                        //请求成功
                        if (response.isSuccess()) {
                            if (listener!=null) {
                                listener.onSuccess(response.getData(),response.getMsg());
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            if (mViewModel!=null){
                                mViewModel.getUC().getErrCodeEvent().postValue(response.getCode());
                            }
                            if (listener!=null){
                                listener.onFail(response.getCode(), response.getMsg(), response);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("错误",e.getMessage());
                        if (e instanceof ResponseThrowable) {
                            if (mViewModel != null) {
                                //((ResponseThrowable) e).message
                                mViewModel.getUC().getShowErrorEvent().postValue(StringUtils.getString(R.string.network_abnormal_please_try_again_later));
                            }

                            if (listener!=null){
                                listener.onError("");
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public Object createServer() {
        return null;
    }

}
