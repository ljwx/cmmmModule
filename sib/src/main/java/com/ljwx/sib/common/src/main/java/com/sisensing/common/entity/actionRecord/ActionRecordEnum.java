package com.sisensing.common.entity.actionRecord;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.sisensing.common.R;

/**
 * 注意获取type对应名字时，使用getName()方法获取。不能直接通过enum.name去获取，否则会调用enum的.name()方法
 * ProjectName: CGM_C
 * Package: com.sisensing.bsmonitoring.actionRecord
 * Author: f.deng
 * CreateDate: 2021/3/23 16:21
 * Description:
 */
public enum ActionRecordEnum {


    FOOD(1, StringUtils.getString(R.string.bsmonitoring_meal_clock_in)),
    SPORTS(2, StringUtils.getString(R.string.common_sports)),
    MEDICATIONS(3, StringUtils.getString(R.string.common_medicine)),
    INSULIN(4, StringUtils.getString(R.string.common_insulin)),
    SLEEP(5, StringUtils.getString(R.string.common_sleep)),
    FINGER_BLOOD(6, StringUtils.getString(R.string.bsmonitoring_finger_blood)),
    PHYSICAL_STATE(7, StringUtils.getString(R.string.common_condition));


    private int type;
    private String name;


    ActionRecordEnum(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }


    public int getType() {
        return type;
    }
}



