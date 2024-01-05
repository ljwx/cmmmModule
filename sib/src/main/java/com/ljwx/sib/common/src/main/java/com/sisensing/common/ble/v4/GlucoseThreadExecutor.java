package com.sisensing.common.ble.v4;

import com.blankj.utilcode.util.LogUtils;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.utils.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GlucoseThreadExecutor {

    private static GlucoseThreadExecutor instance;

    public static GlucoseThreadExecutor getInstance() {
        if (instance == null) {
            instance = new GlucoseThreadExecutor();
        }
        return instance;
    }

    //创建单线程化线程池
    private ExecutorService mSingleThreadExecutor;

    /**
     * 创建或重建线程池
     *
     * @param log1 日志信息1
     * @param log2 日志信息2
     */
    public void createOrRebuildThreadExecutor(String log1, String log2) {
        // shutdownNow()后使用线程池状态来控制插入，还是存在漏洞插入，还是采用等待线程结束更为保险
        if (mSingleThreadExecutor != null) {
            mSingleThreadExecutor.shutdown();
            while (!mSingleThreadExecutor.isTerminated()) {
                LogUtils.i(log1);
                Log.d("上报日志", "蓝牙线程池,"+log1);
                LogUploadModel.getInstance().uploadConnectInfo(log1);
                try {
                    mSingleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LogUtils.i(log2);
            Log.d("上报日志", "蓝牙线程池,"+log2);
            LogUploadModel.getInstance().uploadConnectInfo(log2);
        }
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public void execute(Runnable command) {
        mSingleThreadExecutor.execute(command);
    }
}
