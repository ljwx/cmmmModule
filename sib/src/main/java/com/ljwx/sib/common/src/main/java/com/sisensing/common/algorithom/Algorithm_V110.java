package com.sisensing.common.algorithom;

import com.algorithm.v1_1_0.AlgorithmContext;
import com.algorithm.v1_1_0.NativeAlgorithmLibraryV1_1_0;
import com.alibaba.fastjson.JSONObject;
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
public class Algorithm_V110 implements IAlgorithm {

    //算法中间变量
    private AlgorithmContext mAlgorithmContext;


    @Override
    public int verifyLinkCode(String linkCode) {

        if (mAlgorithmContext == null) {
            mAlgorithmContext = NativeAlgorithmLibraryV1_1_0.getAlgorithmContextFromNative();
        }
        return NativeAlgorithmLibraryV1_1_0.initAlgorithmContext(mAlgorithmContext, linkCode);
    }


    @Override
    public void initAlgorithmContext(DeviceEntity deviceEntity) {
        String algorithmContext = deviceEntity.getAlgorithmContext();
        int algorithmContextIndex = deviceEntity.getAlgorithmContextIndex();

        if (ObjectUtils.isEmpty(algorithmContext)) {
            String deviceName = deviceEntity.getDeviceName();
            if (ObjectUtils.isNotEmpty(deviceName)) {
                String algorithmJson = SPUtils.getInstance(UserInfoUtils.getUserId()).getString(deviceName);
                if (ObjectUtils.isNotEmpty(algorithmJson)) {
                    JSONObject algorithmObject = JSONObject.parseObject(algorithmJson);
                    String versionName = algorithmObject.getString("version_name");
                    //中间变量之前版本version_name存储形式为字符数组，现版本为字符串，此步骤操作目的为兼容之前版本
                    if (versionName.startsWith("[")) {
                        algorithmObject.put("version_name", "ALGORITHM VER 1.0 (2021_05_24C)");
                    }

                    NativeAlgorithmLibraryV1_1_0.setJsonAlgorithmContext(mAlgorithmContext, algorithmObject.toJSONString());
                    LogUploadModel.getInstance().uploadConnectInfo("给算法中间变量赋值：index=" + algorithmContextIndex + "--------->本地已存储的变量值=" + algorithmObject.toJSONString());
                }
            }
        } else {
            NativeAlgorithmLibraryV1_1_0.setJsonAlgorithmContext(mAlgorithmContext, algorithmContext);
            LogUploadModel.getInstance().uploadConnectInfo("给算法中间变量赋值：index=" + algorithmContextIndex + "--------->本地已存储的变量值=" + algorithmContext);
        }

        LogUtils.e("-----------------------给算法中间变量赋值：index=" + algorithmContextIndex);
    }

    /**
     * 保存中间变量
     *
     * @param
     */
    @Override
    public void saveAlgorithmContext(String userId,String deviceName, int index) {
        AppDatabase.getInstance().getDeviceDao().updateAlgorithm(userId,deviceName, NativeAlgorithmLibraryV1_1_0.getJsonAlgorithmContext(mAlgorithmContext), index);
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

        return (float) NativeAlgorithmLibraryV1_1_0.processAlgorithmContext(mAlgorithmContext, index,
                currentData, tempData, bgData, lowAlarmValue, highAlarmValue);

    }


    @Override
    public void releaseAlgorithmContext(String userId,String deviceName) {
        if (mAlgorithmContext != null) {
            NativeAlgorithmLibraryV1_1_0.releaseAlgorithmContext(mAlgorithmContext);

            mAlgorithmContext = null;

            AppDatabase.getInstance().getDeviceDao().updateAlgorithm(userId,deviceName, "", 0);

            SPUtils.getInstance(UserInfoUtils.getUserId()).put(deviceName, "");
        }
    }

    @Override
    public String getAlgorithmVersion() {
        return NativeAlgorithmLibraryV1_1_0.getAlgorithmVersion();
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
