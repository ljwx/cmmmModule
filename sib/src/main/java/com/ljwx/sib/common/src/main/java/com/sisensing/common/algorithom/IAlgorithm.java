package com.sisensing.common.algorithom;

import com.sisensing.common.entity.Device.DeviceEntity;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.algorithom
 * Author: f.deng
 * CreateDate: 2021/8/17 17:37
 * Description:
 */
public interface IAlgorithm {


    void initAlgorithmContext(DeviceEntity deviceEntity);

    /**
     * 初始化算法中间变量
     *
     * @param linkCode
     * @return -1.fail 0.连接码有误  1.success
     */
    int verifyLinkCode(String linkCode);

    /**
     * 保存算法中间变量
     *
     * @param deviceName
     */
    void saveAlgorithmContext(String userId,String deviceName,int index);

    /**
     * 计算血糖值
     *
     * @param index
     * @param currentData
     * @param tempData
     * @param lowAlarmValue
     * @param highAlarmValue
     * @param bgData
     * @return
     */
    float loadData(int index, float currentData, float tempData, float lowAlarmValue,float bgData,float highAlarmValue);

    /**
     * 释放AlgorithmContext对象，可用于重置。
     *
     * @return -1.fail 1.success
     */
    void releaseAlgorithmContext(String userId,String deviceName);

    /**
     * 获取血糖趋势   //0.平稳  1.缓慢上升  -1.缓慢下降  2.较快上升  -2.较快下降
     *
     * @return
     */
    int getGlucoseTrend();

    /**
     * 电流报警  //0.正常  1.过低  2.过高
     *
     * @return
     */
    int getCurrentWarning();

    /**
     * 温度报警  //0.正常  1.过低  2.过高
     *
     * @return
     */
    int getTempWarning();

    /**
     * 血糖报警  // 0.正常  1.过低  2.过高
     *
     * @return
     */
    int getCgmWarning();


    /**
     * sdk版本号
     *
     * @return
     */
    String getAlgorithmVersion();
}
