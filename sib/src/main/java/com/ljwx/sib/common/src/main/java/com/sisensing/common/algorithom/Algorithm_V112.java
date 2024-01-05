package com.sisensing.common.algorithom;

import com.algorithm.v1_1_2.AlgorithmContext;
import com.algorithm.v1_1_2.NativeAlgorithmLibraryV1_1_2;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.user.UserInfoUtils;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/6/21 16:02
 * Description:
 */
public class Algorithm_V112 implements IAlgorithm {

    //算法中间变量
    private AlgorithmContext mAlgorithmContext;


    @Override
    public int verifyLinkCode(String linkCode) {

        if (mAlgorithmContext == null) {
            mAlgorithmContext = NativeAlgorithmLibraryV1_1_2.getAlgorithmContextFromNative();
        }
        return NativeAlgorithmLibraryV1_1_2.initAlgorithmContext(mAlgorithmContext, 0, linkCode);
    }


    @Override
    public void initAlgorithmContext(DeviceEntity deviceEntity) {

        String algorithmContext = deviceEntity.getAlgorithmContext();
        int algorithmContextIndex = deviceEntity.getAlgorithmContextIndex();

        if (ObjectUtils.isNotEmpty(algorithmContext)) {
            NativeAlgorithmLibraryV1_1_2.setJsonAlgorithmContext(mAlgorithmContext, algorithmContext);

            LogUploadModel.getInstance().uploadConnectInfo("给算法中间变量赋值：index=" + algorithmContextIndex + "--------->本地已存储的变量值=" + deviceEntity.getAlgorithmContext());
            LogUtils.e("-----------------------给算法中间变量赋值：index=" + algorithmContextIndex);
        }

    }

    /**
     * 保存中间变量
     *
     * @param
     */
    @Override
    public void saveAlgorithmContext(String userId,String deviceName, int index) {

        AppDatabase.getInstance().getDeviceDao().updateAlgorithm(userId,deviceName, NativeAlgorithmLibraryV1_1_2.getJsonAlgorithmContext(mAlgorithmContext), index);
    }

    /**
     * 免校准血糖算法执行
     *
     * @param index          血糖笔数
     * @param currentData    电流值
     * @param tempData       温度值
     * @param lowAlarmValue  低血糖报警阙值
     * @param highAlarmValue 高血糖报警阙值
     * @return 血糖值
     */
    @Override
    public float loadData(int index, float currentData, float tempData, float bgData, float lowAlarmValue, float highAlarmValue) {
        double currentDataDB = Double.parseDouble(String.valueOf(currentData));
        double tempDataDB = Double.parseDouble(String.valueOf(tempData));
        double bgDataDB = Double.parseDouble(String.valueOf(bgData));
        double lowAlarmValueDB = Double.parseDouble(String.valueOf(lowAlarmValue));
        double highAlarmValueDB = Double.parseDouble(String.valueOf(highAlarmValue));
        double valueDB =  NativeAlgorithmLibraryV1_1_2.processAlgorithmContext(mAlgorithmContext, index,
                currentDataDB, tempDataDB, bgDataDB, lowAlarmValueDB, highAlarmValueDB);
        return Float.parseFloat(String.valueOf(valueDB));
    }


    @Override
    public void releaseAlgorithmContext(String userId,String deviceName) {
        if (mAlgorithmContext != null) {
            NativeAlgorithmLibraryV1_1_2.releaseAlgorithmContext(mAlgorithmContext);

            mAlgorithmContext = null;

            AppDatabase.getInstance().getDeviceDao().updateAlgorithm(userId,deviceName, "", 0);

            SPUtils.getInstance(UserInfoUtils.getUserId()).put(deviceName, "");
        }
    }

    @Override
    public String getAlgorithmVersion() {
        return NativeAlgorithmLibraryV1_1_2.getAlgorithmVersion();
    }

    @Override
    public int getGlucoseTrend() {
        return mAlgorithmContext.ig_trend;
    }

    @Override
    public int getCurrentWarning() {
        return mAlgorithmContext.currentWarning;
    }

    @Override
    public int getTempWarning() {
        return mAlgorithmContext.temperatureWarning;
    }

    @Override
    public int getCgmWarning() {
        return mAlgorithmContext.glucoseWarning;
    }

}
