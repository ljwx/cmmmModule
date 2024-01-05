package com.sisensing.common.utils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEnum;
import com.sisensing.common.listener.IsClockInListener;
import com.sisensing.common.user.UserInfoUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author y.xie
 * @date 2021/6/15 11:01
 * @desc 打卡相关
 */
public class ClockInUtils {
    /**
     * 对应的时间戳一个小时内是否打过卡
     * @param isWear 是否是穿戴设备
     * @param clockContent 打卡内容
     * @param currentMills 当前时间
     * @param type 打卡类型
     * @param listener 回调监听
     */
    public static void oneHourIsClockIn(boolean isWear,String clockContent,long currentMills,int type,IsClockInListener listener){
        long startMill = CommonTimeUtils.getStartTime(new Date(currentMills)).getTime();
        long endMill = CommonTimeUtils.getEndTime(new Date(currentMills)).getTime();

        RoomTask.singleTask(AppDatabase.getInstance().getActionRecordEntityDao().getActionRecord(UserInfoUtils.getUserId(),
                startMill, endMill, type), new RoomResponseListener<List<ActionRecordEntity>>() {
            @Override
            public void response(@Nullable List<ActionRecordEntity> list) {
                if (ObjectUtils.isEmpty(list)){
                    if (isWear){
                        listener.isClockIn(false,null);
                    }else {
                        showConfirmPop(listener,false,clockContent,null);
                    }
                }else {
                    //当前某个小时内是否已经打卡
                    boolean isClockIn = false;
                    //不是重复打卡的数据
                    List<ActionRecordEntity> notRepeatEntityList = new ArrayList<>();
                    for (ActionRecordEntity entity:list){
                        if (entity.getOneHoursRepeatClockIn() == 0){
                            //找到所有不是重复打卡的数据
                            notRepeatEntityList.add(entity);
                        }
                    }

                    if (ObjectUtils.isNotEmpty(notRepeatEntityList)){
                        for (ActionRecordEntity entity : notRepeatEntityList) {
                            long mill = entity.getStartTime();
                            if (Math.abs(mill - currentMills)<=60*60*1000){
                                isClockIn = true;
                                break;
                            }
                        }
                    }

                    if (isWear){
                        listener.isClockIn(isClockIn,list);
                    }else {
                        showConfirmPop(listener,isClockIn,clockContent,list);
                    }
                }
            }
        });
    }

    private static void showConfirmPop(IsClockInListener listener,boolean isClockIn,String content,List<ActionRecordEntity> list){
        new XPopup.Builder(ActivityUtils.getTopActivity())
                .asConfirm("", content,  new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        listener.isClockIn(isClockIn,list);
                    }
                }).show();
    }
}
