package com.ljwx.sib.base.src.main.java.com.sisensing.base;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.ObjectUtils;
import com.ljwx.baseapp.vm.BaseAndroidViewModel;
import com.sisensing.event.SingleLiveEvent;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;
import java.util.Map;


public class BaseViewModel<M extends BaseModel<?>> extends BaseAndroidViewModel<M> implements IBaseViewModel {

    public M model;
    private UIChangeLiveData uc;
    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;

    public BaseViewModel(@NonNull Application application) {
        super(application);

        model = createModel();
    }


    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    public UIChangeLiveData getUC() {
        if (uc == null) {
            uc = new UIChangeLiveData();
        }
        return uc;
    }


    public void showLoading(String message) {
        uc.showLoadingEvent.postValue(message);
    }

    public void showError(String message) {
        uc.showErrorEvent.postValue(message);
    }

    public void showSuccess(String message) {
        uc.showSuccessEvent.postValue(message);
    }

    public void errCodeDeal(int code){
        uc.errCodeEvent.postValue(code);
    }

    public void showTipToast(String message){
        if (ObjectUtils.isNotEmpty(message)) {
            uc.showTipEvent.postValue(message);
        }
    }

    public void dismissDialog() {
        uc.dismissDialogEvent.call();
    }

    /**
     * 关闭界面
     */
    public void finish() {
        Log.i("gao","调用了baseViewModel 的 finish");
        uc.finishEvent.setValue(null);

    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        uc.onBackPressedEvent.call();
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        super.onCreate(owner);
        onCreate();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        onDestroy();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        super.onStart(owner);
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        super.onStop(owner);
        onStop();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        super.onResume(owner);
        onResume();
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        super.onPause(owner);
        onPause();
    }

    @Override
    public void onPause() {
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.onCleared();
        }
    }


    protected M createModel() {

        return null;
    }

    @Override
    public M createRepository() {
        return null;
    }


    public final class UIChangeLiveData extends SingleLiveEvent {
        private SingleLiveEvent<String> showLoadingEvent;
        private SingleLiveEvent<String> showSuccessEvent;
        private SingleLiveEvent<String> showErrorEvent;
        private SingleLiveEvent<String> showTipEvent;
        //网络结果错误Code处理
        private SingleLiveEvent<Integer> errCodeEvent;

        private SingleLiveEvent<Void> dismissDialogEvent;
        private SingleLiveEvent<Map<String, Object>> startActivityEvent;
        private SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;

        //这里不用single,
        private MutableLiveData<Void> finishEvent;
        private SingleLiveEvent<Void> onBackPressedEvent;

        public SingleLiveEvent<String> getShowLoadingEvent() {
            return showLoadingEvent = createLiveData(showLoadingEvent);
        }

        public SingleLiveEvent<String> getShowSuccessEvent() {
            return showSuccessEvent = createLiveData(showSuccessEvent);
        }

        public SingleLiveEvent<String> getShowErrorEvent() {
            return showErrorEvent = createLiveData(showErrorEvent);
        }

        public SingleLiveEvent<Integer> getErrCodeEvent() {
            return errCodeEvent = createLiveData(errCodeEvent);
        }


        public SingleLiveEvent<String> getShowTipEvent() {
            return showTipEvent = createLiveData(showTipEvent);
        }

        public SingleLiveEvent<Void> getDismissDialogEvent() {
            return dismissDialogEvent = createLiveData(dismissDialogEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
            return startActivityEvent = createLiveData(startActivityEvent);
        }

        public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
            return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
        }

        public MutableLiveData<Void> getFinishEvent() {
            return finishEvent = (finishEvent == null?new MutableLiveData<>():finishEvent);
        }

        public SingleLiveEvent<Void> getOnBackPressedEvent() {
            return onBackPressedEvent = createLiveData(onBackPressedEvent);
        }

        private <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
            if (liveData == null) {
                liveData = new SingleLiveEvent<>();
            }
            return liveData;
        }

        @Override
        public void observe(LifecycleOwner owner, Observer observer) {
            super.observe(owner, observer);
        }
    }
}
