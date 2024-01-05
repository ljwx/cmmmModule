package com.sisensing.common.utils;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ObjectUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.user.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 小组件数据处理类
 */
public class AppWidgetData {

    private Context mContext;
    //最近的一次高低血檀类型
    private int recentEventType = 1;
    //是否需要查询数据库
    private boolean isNeedQuerySql = true;


    public AppWidgetData(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 处理血糖数据
     *
     * @param bloodGlucoseEntity
     */
    public void dealBsData(BloodGlucoseEntity bloodGlucoseEntity) {

        int index = bloodGlucoseEntity.getIndex();
        int numOfUnreceived = bloodGlucoseEntity.getNumOfUnreceived();

        if (index <= 60 && numOfUnreceived <= 60) {
            //首次连接设备
            if (index == 60) {
                sendBroadCastToWidget(bloodGlucoseEntity, 1);
            }
        } else {
            //非首次连接
            //未发送笔数(最小值为1,代表没有未发送笔数了)
            if (numOfUnreceived <= 1) {
               if (bloodGlucoseEntity.getIndex() % 5 == 0){
                   sendBroadCastToWidget(bloodGlucoseEntity, 1);
               }else {
                   //防止同步完但是当前血糖不是第五笔，所以查询一次数据库
                   if (isNeedQuerySql){
                       isNeedQuerySql = false;
                       showLastValidBsData();
                   }
               }
            }else {
                isNeedQuerySql = true;
            }
        }
    }

    /**
     *
     * @param bloodGlucoseEntity
     * @param dataType 1.有数据 2.无数据
     */
    private void sendBroadCastToWidget(BloodGlucoseEntity bloodGlucoseEntity, int dataType) {

        if (dataType == 1) {
            // TODO: 2022/3/21 这里发现当插入数据库然后再查询数据库可能会存在不能立刻查出最新的这笔血糖
            BsDataUtils.getCurrentDateBsData(bloodGlucoseEntities -> {
                BloodGlucoseEntity recentEventEntity = null;
                String tir;
                List<BloodGlucoseEntity> list = new ArrayList<>();

                if (ObjectUtils.isEmpty(bloodGlucoseEntities)) {
                    tir = "-.-";
                } else {
                    List<BloodGlucoseEntity> newList = BsDataUtils.deleteExceptionBsData(bloodGlucoseEntities);
                    if (ObjectUtils.isEmpty(newList)) {
                        tir = "-.-";
                    } else {
                        //计算TIR
                        float[] bsArray = new float[newList.size()];
                        //tir百分比
                        float[] tirArray = new float[5];
                        //tir时长
                        long[] tirTimeArray = new long[5];
                        for (int i = 0; i < newList.size(); i++) {
                            bsArray[i] = newList.get(i).getGlucoseValue();
                        }
                        //调用算法获取对应tir
                        BsCalcUtils.getTir(bsArray, tirArray, tirTimeArray);
                        tir = BigDecimalUtils.round(tirArray[2] * 100, 1) + "%";
                        list.addAll(newList);
                    }
                }
                //当前时间
                long currentMills = System.currentTimeMillis();
                //最近一小时的高血糖集合
                List<BloodGlucoseEntity> recentHourHighList = new ArrayList<>();
                //最近一小时的低血糖集合
                List<BloodGlucoseEntity> recentHourLowerList = new ArrayList<>();
                //报警上下限
                float alarmUpper = UserInfoUtils.getBsAlarmUpper();
                float alarmLower = UserInfoUtils.getBsAlarmLower();

                int alarmStatus = bloodGlucoseEntity.getAlarmStatus();

                if (alarmStatus == 1 || alarmStatus == 5 || alarmStatus == 6){
                    if (ObjectUtils.isEmpty(list)){
                        list.add(bloodGlucoseEntity);
                    }else {
                        BloodGlucoseEntity localLastEntity = list.get(list.size()-1);
                        if (localLastEntity.getProcessedTimeMill() != bloodGlucoseEntity.getProcessedTimeMill()){
                            //不是同一笔血糖直接添加到当前集合
                            list.add(bloodGlucoseEntity);
                        }
                    }
                }
                if (ObjectUtils.isNotEmpty(list)){
                    for (BloodGlucoseEntity entity: list) {
                        long mills = entity.getProcessedTimeMill();
                        float bsValue = entity.getGlucoseValue();
                        //找出一个小时之内后的所有血糖
                        if ( mills >= currentMills - 60*60*1000){
                            if (bsValue < alarmLower){
                                recentHourLowerList.add(entity);
                            }

                            if (bsValue > alarmUpper){
                                recentHourHighList.add(entity);
                            }
                        }
                    }
                    //最近的一次高血糖
                    BloodGlucoseEntity recentHighEntity = null;
                    //最近的一次低血糖
                    BloodGlucoseEntity recentLowerEntity = null;

                    if (ObjectUtils.isNotEmpty(recentHourHighList)){
                        recentHighEntity = recentHourHighList.get(recentHourHighList.size()-1);
                    }

                    if (ObjectUtils.isNotEmpty(recentHourLowerList)){
                        recentLowerEntity = recentHourLowerList.get(recentHourLowerList.size()-1);
                    }

                    if (recentHighEntity!=null && recentLowerEntity == null){
                        recentEventEntity = recentHighEntity;
                        recentEventType = 1;
                    }

                    if (recentHighEntity == null && recentLowerEntity!=null){
                        recentEventEntity = recentLowerEntity;
                        recentEventType = 2;
                    }

                    if (recentHighEntity!=null && recentLowerEntity!=null){
                        long highMills = recentHighEntity.getProcessedTimeMill();
                        long lowerMills = recentLowerEntity.getProcessedTimeMill();

                        //最近的一次血糖
                        if (highMills > lowerMills){
                            recentEventEntity = recentHighEntity;
                            recentEventType = 1;
                        }else {
                            recentEventEntity = recentLowerEntity;
                            recentEventType = 2;
                        }
                    }
                }
//                Intent updateWidgetIntent = new Intent();
//                //指定广播行为动作的名字
//                updateWidgetIntent.setAction(Constant.WIDGET_UPDATE_ACTION);
//                //传递tir
//                updateWidgetIntent.putExtra(Constant.WIDGET_TIR, tir);
//                updateWidgetIntent.putExtra(Constant.WIDGET_DATA_TYPE, dataType);
//                //传递血糖
//                updateWidgetIntent.putExtra(Constant.WIDGET_BLOOD_GLUCOSE_ENTITY, bloodGlucoseEntity);
//                if (recentEventEntity!=null){
//                    updateWidgetIntent.putExtra(Constant.WIDGET_EVENT_TYPE,recentEventType);
//                    updateWidgetIntent.putExtra(Constant.WIDGET_EVENT_ENTITY,recentEventEntity);
//                }
//                //发送广播
//                mContext.sendBroadcast(updateWidgetIntent);
            });
        } else {
//            Intent updateWidgetIntent = new Intent();
//            //指定广播行为动作的名字
//            updateWidgetIntent.setAction(Constant.WIDGET_UPDATE_ACTION);
//            //传递数据类型
//            updateWidgetIntent.putExtra(Constant.WIDGET_DATA_TYPE, dataType);
//            //发送广播
//            mContext.sendBroadcast(updateWidgetIntent);
        }

    }


    /**
     * 展示最后一笔数据
     *
     */
    public void showLastValidBsData() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;

        String deviceName = deviceEntity.getDeviceName();
        if (ObjectUtils.isEmpty(deviceName)) return;

        String userId = UserInfoUtils.getUserId();
        if (ObjectUtils.isEmpty(userId)) return;

        //获取最后一笔有效血糖
//        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastValidBloodGlucose(userId, deviceName,Constant.BS_INDEX_MAX),
//                new RoomResponseListener<BloodGlucoseEntity>() {
//                    @Override
//                    public void response(@Nullable BloodGlucoseEntity bloodGlucoseEntity) {
//                        if (bloodGlucoseEntity == null) return;
//                        sendBroadCastToWidget(bloodGlucoseEntity, 1);
//                    }
//                });
    }

    /**
     * 清除小组件数据
     */
    public void cleanWidgetData() {
        sendBroadCastToWidget(null, 2);
    }
}
