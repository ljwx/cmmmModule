package com.sisensing.common.database;


import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.database
 * @Author: f.deng
 * @CreateDate: 2021/3/9 17:01
 * @Description:
 */
public class RoomTask {


    public static <T> void singleTask(Single<T> tSingle, final RoomResponseListener<T> listener) {

        tSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(T t) {
                        if (listener != null) {
                            listener.response(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.response(null);
                        }
                    }
                });

    }

    public static <T> void singleSyncTask(Single<T> tSingle, final RoomResponseListener<T> listener) {

        tSingle.subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(T t) {
                        if (listener != null) {
                            listener.response(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.response(null);
                        }
                    }
                });

    }

    public static void CompletableTask(Completable tCompletable) {
        tCompletable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });


    }


    public <T> void addDisposable(Flowable<T> flowable, Consumer<T> consumer) {
        flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    public <T> void addDisposable(Completable completable, Action action) {
        completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

}
