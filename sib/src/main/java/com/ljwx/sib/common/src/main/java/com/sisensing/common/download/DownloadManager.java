package com.sisensing.common.download;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.base.RetrofitUtils;
import com.sisensing.common.net.DownloadApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.download
 * @Author: l.chenlu
 * @CreateDate: 2021/6/16 17:11
 * @Description:
 */
public class DownloadManager {
    private String url ="";
    private String saveUrl = "";
    private OnDownloadListener listener;
    private boolean isCheckFileExist;
    private static final int HANDLER_START = 0;
    private static final int HANDLER_NEXT = 1;
    private static final int HANDLER_FINISH = 2;

    private DownloadHandler handler = new DownloadHandler(this);
    private static class DownloadHandler extends Handler{

        private WeakReference<DownloadManager> weakReference;

        public DownloadHandler(DownloadManager downloadManager){
            super(Looper.getMainLooper());
            weakReference = new WeakReference<>(downloadManager);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DownloadManager downloadManager = weakReference.get();
            switch (msg.what){
                case HANDLER_START:
                    if(downloadManager.listener!=null){
                        downloadManager.listener.onStart();
                    }
                    break;
                case HANDLER_NEXT:
                    if(downloadManager.listener!=null){
                        downloadManager.listener.onProgrss(msg.arg1);
                    }
                    break;
                case HANDLER_FINISH:
                    if(downloadManager.listener!=null){
                        downloadManager.listener.onFinish();
                    }
                    break;
            }
        }
    }

    public DownloadManager (String url,String saveUrl,boolean isCheckFileExist,OnDownloadListener listener){
        this.url = url;
        this.saveUrl = saveUrl;
        this.isCheckFileExist = isCheckFileExist;
        this.listener = listener;
    }

    public void down() {

        if (ObjectUtils.isEmpty(url)||ObjectUtils.isEmpty(saveUrl)){
            return;
        }
        Observable<ResponseBody> down = RetrofitUtils.getInstance().getRequest(DownloadApiService.class).downloadFileByUrl(url);
        down.subscribeOn(Schedulers.io())
                //因为要写文件,所以Observer不切换到主线程
                .subscribe(new Observer<ResponseBody>() {
                    private int progrss;

                    @Override
                    public void onSubscribe(Disposable d) {
                        if(listener!=null){
                            handler.sendEmptyMessage(HANDLER_START);
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        FileOutputStream fileOutputStream = null;
                        long maxx = responseBody.contentLength();

                        File file = new File(saveUrl);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            if (isCheckFileExist&&file.exists()&&file.length()==maxx){
                                return;
                            }
                        }
                        try {
                            fileOutputStream = new FileOutputStream(file);

                            byte[] bytes = new byte[1024];

                            int rendLength = 0;
                            long currLength = 0;

                            while ((rendLength = inputStream.read(bytes)) != -1) {
                                fileOutputStream.write(bytes, 0, rendLength);
                                currLength += rendLength;

                                progrss = (int) (currLength * 100 / maxx);
                                if(listener!=null){
                                    //listener.onProgrss(progrss);
                                    Message msg = Message.obtain();
                                    msg.what = HANDLER_NEXT;
                                    msg.arg1 = progrss;
                                    handler.sendMessage(msg);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (listener != null) {
                                listener.onException(e);
                            }
                        }finally {
                            try {
                                if (inputStream!=null){
                                    inputStream.close();
                                }
                                if(fileOutputStream!=null){
                                    fileOutputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (listener != null) {
                                    listener.onException(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.onException(e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(listener!=null){
                            //listener.onFinish();
                            handler.sendEmptyMessage(HANDLER_FINISH);
                        }
                    }
                });
    }

    public static class Builder{
        private String url ="";
        private String saveUrl = "";
        private OnDownloadListener listener;
        private boolean isCheckFileExist;

        public Builder(){

        }

        public Builder setUrl(String url){
            this.url = url;
            return this;
        }

        public Builder setSaveUrl(String saveUrl){
            this.saveUrl = saveUrl;
            return this;
        }

        public Builder setListener(OnDownloadListener listener){
            this.listener = listener;
            return this;
        }

        public Builder setIsCheckFileExist(boolean isCheckFileExist){
            this.isCheckFileExist = isCheckFileExist;
            return this;
        }

        public DownloadManager builder(){
            return new DownloadManager(url,saveUrl,isCheckFileExist,listener);
        }
    }
}
