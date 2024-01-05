package com.ljwx.basemodule.ble;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.ble.CgmStatusListener;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.BsDataUtils;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.ble
 * Author: f.deng
 * CreateDate: 2021/7/26 17:15
 * Description:
 */
public class CgmStatusWrapper {

    //是否第一次获得未读笔数(判断是否是未读总笔数的标识)
    private boolean isFirstGetNumOfUnreceived = true;
    //状态监听
    private CgmStatusListener mStatusListener;
    //未读笔数
    private int totalNumOfUnreceived;


    public CgmStatusWrapper(CgmStatusListener statusListener) {
        mStatusListener = statusListener;
    }

    public void setFirstGetNumOfUnreceived(boolean firstGetNumOfUnreceived) {
        isFirstGetNumOfUnreceived = firstGetNumOfUnreceived;
    }

    public void handleBloodGlucose(BloodGlucoseEntity bloodGlucoseEntity) {
        if (mStatusListener == null) return;
        int index = bloodGlucoseEntity.getIndex();
        int numOfUnreceived = bloodGlucoseEntity.getNumOfUnreceived();


        if (isFirstGetNumOfUnreceived) {
            totalNumOfUnreceived = numOfUnreceived;
            if (totalNumOfUnreceived>=20160){
                totalNumOfUnreceived = 20160;
            }
            isFirstGetNumOfUnreceived = false;
        }
        LogUtils.i("BloodGlucoseEntity","glucoseValue:"+bloodGlucoseEntity.getGlucoseValue()
                + "\nindex:" + bloodGlucoseEntity.getIndex()+"\nnumOfUnreceived:"+numOfUnreceived+"\ntotalNumOfUnreceived:"+totalNumOfUnreceived);
        if (index <= 60 && totalNumOfUnreceived <= 60) {
            //首次连接设备
            mStatusListener.cgmDataInit(index);
            mStatusListener.cgmDataOther(bloodGlucoseEntity);
            UserInfoUtils.isSync = true;

            if (index == 60) {
                UserInfoUtils.isSync = false;
                mStatusListener.cgmDataAlarm(bloodGlucoseEntity);
                showOriginalBsDataView(bloodGlucoseEntity);
            }
        } else {
            //非首次连接
            //未发送笔数(最小值为1,代表没有未发送笔数了)
            if (numOfUnreceived > 1) {
                UserInfoUtils.isSync = true;
                //数据未同步准备同步
                int received ;
                String syncPercent;
                if (totalNumOfUnreceived == 20160){
                    received = index;
                }else {
                    received = totalNumOfUnreceived - numOfUnreceived;
                }
                syncPercent  = BsDataUtils.getPercent(received, totalNumOfUnreceived, 0);
                mStatusListener.cgmDataSync(totalNumOfUnreceived, received, syncPercent, bloodGlucoseEntity);

                int alarmStatus = bloodGlucoseEntity.getAlarmStatus();
                if (index>=60 && index % 5 == 0 && (alarmStatus == 1 || alarmStatus == 5 || alarmStatus == 6)) {
                    mStatusListener.cgmDataCurve(bloodGlucoseEntity);
                }
            } else {
                UserInfoUtils.isSync = false;

                mStatusListener.cgmDataWear(bloodGlucoseEntity);
                showOriginalBsDataView(bloodGlucoseEntity);
            }
            //同步报警数量
            mStatusListener.cgmDataAlarm(bloodGlucoseEntity);
        }
    }

    /**
     * 每分钟血糖都经过此方法(前59笔和同步时除外)
     *
     * @param bloodGlucoseEntity
     */
    private void showOriginalBsDataView(BloodGlucoseEntity bloodGlucoseEntity) {
        mStatusListener.cgmDataOther(bloodGlucoseEntity);

        int alarmStatus = bloodGlucoseEntity.getAlarmStatus();
        if (alarmStatus == 2) {
            //30分钟的电流异常
            mStatusListener.cgmDataAbnormalCurrent();
        } else {
            //当血糖数值超过区间（2.2-25 mmol/L）时，不再显示数字，显示文字提示：血糖极低/极高
            //血糖最大区间值

            if (alarmStatus == 3 || alarmStatus == 4) {
                //温度过高或过低
                mStatusListener.cgmDataHighOrLowTp(bloodGlucoseEntity);
            } else {
                //温度正常
                if (bloodGlucoseEntity.getIndex() % 5 == 0) {
                    showNormalBsValue(bloodGlucoseEntity);
                } else {
                    showLastValidBsData();
                }
            }
        }
    }

    /**
     * 展示每5分钟一笔的血糖
     *
     * @param bloodGlucoseEntity
     */
    private void showNormalBsValue(BloodGlucoseEntity bloodGlucoseEntity) {

        //数据已同步，正常显示有效血糖值
        //报警状态
        int alarmStatus = bloodGlucoseEntity.getAlarmStatus();

        if (alarmStatus == 5) {
            //血糖极高
            mStatusListener.cgmDataHighBs(bloodGlucoseEntity);
        } else if (alarmStatus == 6) {
            //血糖极低
            mStatusListener.cgmDataLowBs(bloodGlucoseEntity);
        } else {
            //最低血糖和最高血糖之间
            mStatusListener.cgmDataShow(bloodGlucoseEntity);

        }
        mStatusListener.cgmDataCurve(bloodGlucoseEntity);
    }

    public void showLastValidBsData() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;

        String deviceName = deviceEntity.getDeviceName();
        if (ObjectUtils.isEmpty(deviceName)) return;

        String userId = UserInfoUtils.getUserId();
        if (ObjectUtils.isEmpty(userId)) return;

        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastBloodGlucose(userId, deviceName), new RoomResponseListener<BloodGlucoseEntity>() {
            @Override
            public void response(@Nullable BloodGlucoseEntity bloodGlucoseEntity) {
                if (bloodGlucoseEntity == null) {
                    //显示倒计时
                    mStatusListener.cgmDataInit(1);
                } else {
                    int index = bloodGlucoseEntity.getIndex();
                    if (index < 60) {
                        mStatusListener.cgmDataInit(index);
                    } else {
                        if (bloodGlucoseEntity.getAlarmStatus() == 2) {
                            mStatusListener.cgmDataAbnormalCurrent();
                        } else {
                            //获取最后一笔有效血糖
                            RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastValidBloodGlucose(userId, deviceName),
                                    new RoomResponseListener<BloodGlucoseEntity>() {
                                        @Override
                                        public void response(@Nullable BloodGlucoseEntity bloodGlucoseEntity) {
                                            if (bloodGlucoseEntity == null) return;
                                            showOriginalBsDataView(bloodGlucoseEntity);
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }
}
