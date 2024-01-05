package com.ljwx.basemodule.ble;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;

import com.blankj.utilcode.util.LogUtils;
import com.sisensing.common.base.ResponseListener;
import com.sisensing.common.ble.BsUploadServiceModel;

public class BsUploadService extends JobService {

    private BsUploadServiceModel mServiceModel;

    private static final int MESSAGE_ID = 100;
    public static final int JOB_ID_UPLOAD = 1000;
    private static final String EXTRA_DEVICE_NAME = "deviceName";
    private static final String EXTRA_DEVICE_ID = "deviceID";
    private static final String EXTRA_DEVICE_ALGORITHM_VERSION = "deviceAlgorithmVersion";


    public static PersistableBundle createExtras(String deviceName, String deviceId, String algorithmVersion) {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(EXTRA_DEVICE_NAME, deviceName);
        persistableBundle.putString(EXTRA_DEVICE_ID, deviceId);
        persistableBundle.putString(EXTRA_DEVICE_ALGORITHM_VERSION, algorithmVersion);
        return persistableBundle;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogUtils.e("handle message!!!!");
            //请注意，我们手动调用了jobFinished方法。
            //当onStartJob返回true的时候，我们必须在合适时机手动调用jobFinished方法
            //否则该应用中的其他job将不会被执行
            jobFinished((JobParameters) msg.obj, false);
            //第一个参数JobParameter来自于onStartJob(JobParameters params)中的params，
            // 这也说明了如果我们想要在onStartJob中执行异步操作，必须要保存下来这个JobParameter。
            return true;
        }
    });


    @Override
    public void onCreate() {
        if (mServiceModel == null) {
            mServiceModel = new BsUploadServiceModel();
        }

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.e("onStartJob");
        final PersistableBundle bundle = params.getExtras();
        String deviceName = bundle.getString(EXTRA_DEVICE_NAME);
        String deviceId = bundle.getString(EXTRA_DEVICE_ID);
        String localAlgorithmVersion = bundle.getString(EXTRA_DEVICE_ALGORITHM_VERSION);
        mServiceModel.getCurrentIndex(deviceName, deviceId, localAlgorithmVersion, new ResponseListener() {
            @Override
            public void onSuccess(Object data, String msg) {

                LogUtils.e("onSuccess"+msg);
                final Message message = mJobHandler.obtainMessage();
                message.what = params.getJobId();
                message.obj = params;
                message.sendToTarget();
            }

            @Override
            public void onFail(int code, String message, Object errorData) {
                LogUtils.e("onFail"+message);
            }

            @Override
            public void onError(String message) {
                LogUtils.e("onError"+message);
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.e("onStopJob");
        mJobHandler.removeMessages(MESSAGE_ID);

        return false;
    }
}
