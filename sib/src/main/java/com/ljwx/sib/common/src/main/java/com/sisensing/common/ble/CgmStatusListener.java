package com.sisensing.common.ble;

import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/7/22 11:41
 * Description:
 */
public interface CgmStatusListener {


    //实时每五分钟的数据
    void updateData(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器数据初始化
    void cgmDataInit(int index);

    //传感器数据同步中
    void cgmDataSync(int total, int received, String syncPercent, BloodGlucoseEntity bloodGlucoseEntity);

    //传感器数据电流异常
    void cgmDataAbnormalCurrent();

    //传感器温度过高或过低
    void cgmDataHighOrLowTp(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器血糖过高
    void cgmDataHighBs(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器血糖过低
    void cgmDataLowBs(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器正常血糖数据
    void cgmDataShow(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器失效或异常(已损坏)
    void cgmDataValidOrExcept();

    //传感器警报
    void cgmDataAlarm(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器与手表之间的数据处理
    void cgmDataWear(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器其他处理(剩余时间...)
    void cgmDataOther(BloodGlucoseEntity bloodGlucoseEntity);

    //传感器曲线
    void cgmDataCurve(BloodGlucoseEntity bloodGlucoseEntity);

    //与设备数据同步(每一笔数据都发送出去,不考虑任何情况)
    void cgmDataSyncEvery(BloodGlucoseEntity bloodGlucoseEntity);
}
