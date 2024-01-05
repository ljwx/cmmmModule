package com.sisensing.common.ble.v4;

import com.sisensing.common.algorithom.IAlgorithm;
import com.sisensing.common.ble.v4.data.GlucoseDataInfo;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.ConfigUtils;

import no.sisense.android.bean.CGMRecordV120;
import no.sisense.android.bean.GjCGMRecord;

public class BloodGlucoseEntityUtils {

    public static <T> BloodGlucoseEntity cgmRecordTransAppEntity(IAlgorithm mIAlgorithm, T cgmRecord, long firstTime) {
        return cgmRecordTransAppEntity(mIAlgorithm, cgmRecord, firstTime, false);
    }

    public static <T> BloodGlucoseEntity cgmRecordTransAppEntity(IAlgorithm mIAlgorithm, T cgmRecord, long firstTime, boolean sku) {

        String userId = UserInfoUtils.getUserId();

        //数据索引
        int index = 0;
        //温度
        float temp = 0f;
        //电量
        long electric = 0;
        //血糖状态
        int status = 0;
        //电流值
        float value = 0f;
        //未读笔数
        int numUnreceived = 0;

        if (cgmRecord instanceof GjCGMRecord) {
            index =((GjCGMRecord) cgmRecord).getIndex();
            temp = ((GjCGMRecord) cgmRecord).getTemp()/(sku ? 10f : 1f);
            electric = ((GjCGMRecord) cgmRecord).getElectric();
            status = ((GjCGMRecord) cgmRecord).getStatus();
            value = ((GjCGMRecord) cgmRecord).getValue();
            numUnreceived = ((GjCGMRecord) cgmRecord).getNumOfUnreceived();
        } else if (cgmRecord instanceof CGMRecordV120) {
            index = ((CGMRecordV120) cgmRecord).getIndex();
            temp = ((CGMRecordV120) cgmRecord).getTemp()/10f;
            electric = ((CGMRecordV120) cgmRecord).getDump();
            value = ((CGMRecordV120) cgmRecord).getCurrent()/10f;
            numUnreceived = ((CGMRecordV120) cgmRecord).getReindex();
        }

//        //数据索引
//        int index = cgmRecord.getIndex();
//        //温度
//        float temperatureValue = cgmRecord.getTemp() / (sku ? 10f : 1f);
//        //电量
//        long electric = cgmRecord.getElectric();
//        //血糖状态
//        int stateValue = cgmRecord.getStatus();
//        //电流值
//        float currentValue = cgmRecord.getValue();

        BloodGlucoseEntity glucoseEntity = new BloodGlucoseEntity();

        glucoseEntity.setIndex(index);
        glucoseEntity.setBleName(SensorDeviceInfo.mDeviceName);
        glucoseEntity.setMacAddress(SensorDeviceInfo.mMacAddress);
        glucoseEntity.setTemperatureValue(temp);
        glucoseEntity.setCurrentValue(value);
        glucoseEntity.setStateValue(status);
        glucoseEntity.setNumOfUnreceived(GlucoseDataInfo.mNumUnReceived);
        glucoseEntity.setElectric(electric);
        glucoseEntity.setUserId(userId);

        float defaultHigh = ConfigUtils.getInstance().getDefaultHigh(userId);
        float defaultLow = ConfigUtils.getInstance().getDefaultLow(userId);

        //血糖值
        float glucoseValue = mIAlgorithm.loadData(index, value, temp, 0, defaultLow, defaultHigh);
        //血糖趋势
        int glucoseTrend = mIAlgorithm.getGlucoseTrend();
        //电流报警
        int currentWarning = mIAlgorithm.getCurrentWarning();
        //温度报警
        int tempWarning = mIAlgorithm.getTempWarning();
        //血糖报警
        int cgmWarning = mIAlgorithm.getCgmWarning();

        //对血糖值进行处理，低于2.2的赋值为2.2,高于25的赋值为25
        if(glucoseValue < 2.2){
            glucoseValue = 2.2f;
        }
        if(glucoseValue >= 25){
            glucoseValue = 25f;
        }

        glucoseEntity.setGlucoseValue(glucoseValue);
        glucoseEntity.setTemperatureWarning(tempWarning);
        glucoseEntity.setGlucoseWarning(cgmWarning);
        glucoseEntity.setCurrentWarning(currentWarning);
        glucoseEntity.setGlucoseTrend(glucoseTrend);
        glucoseEntity.setValidIndex(index % 5 == 0 ? index / 5 : 0);

        SensorDeviceInfo.sensorIsEdOrEp = false;

        BloodGlucoseEntityUtils.setTemperature(glucoseEntity, index, tempWarning);

        if (index == 1) {
            GlucoseDataInfo.mProcessedTimeMill = firstTime;
        } else {
            GlucoseDataInfo.mProcessedTimeMill += 60000L;
        }
        GlucoseDataInfo.mSyncTimeMill = System.currentTimeMillis();
        glucoseEntity.setProcessedTimeMill(GlucoseDataInfo.mProcessedTimeMill);

        return glucoseEntity;
    }


    public static void setTemperature(BloodGlucoseEntity glucoseEntity, int index, int errTemperature) {
        if (errTemperature == 1) {
            //温度过低
            glucoseEntity.setAlarmStatus(4);
        } else if (errTemperature == 2) {
            //温度过高
            glucoseEntity.setAlarmStatus(3);
        } else {
            //温度正常
            if (index % 5 == 0) {
                if (glucoseEntity.getGlucoseValue() < 2.2f) {
                    //血糖过低
                    glucoseEntity.setAlarmStatus(6);
                } else if (glucoseEntity.getGlucoseValue() > 25f) {
                    //血糖过高
                    glucoseEntity.setAlarmStatus(5);
                } else {
                    //血糖正常
                    glucoseEntity.setAlarmStatus(1);
                }
            } else {
                glucoseEntity.setAlarmStatus(1);
            }
        }
    }

    public static long getFirstTimeStamp(GjCGMRecord cgmRecord) {
        return System.currentTimeMillis() - 60 * 1000L * (cgmRecord.getIndex() + cgmRecord.getNumOfUnreceived()) + 60 * 1000L;
    }

}
