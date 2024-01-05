package com.sisensing.common.view.popup;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lxj.xpopup.core.AttachPopupView;
import com.sisensing.common.R;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEnum;
import com.sisensing.common.utils.AppLanguageUtils;
import com.sisensing.common.utils.BgUnitUtils;
import com.sisensing.common.utils.GlucoseUtils;

/**
 * @author y.xie
 * @date 2021/6/21 11:48
 * @desc 行为打卡弹窗
 */
public class ActionClockInPop extends AttachPopupView {
    private ActionRecordEntity actionRecordEntity;
    private TextView mTvAction;
    private TextView mTvTime;
    private TextView mTvOther;

    public ActionClockInPop(@NonNull Context context) {
        super(context);
    }

    public void setActionRecordEntity(ActionRecordEntity actionRecordEntity) {
        this.actionRecordEntity = actionRecordEntity;
        showInfo();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.common_action_clock_in_markview;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        mTvAction = findViewById(R.id.tv_action);

        mTvTime = findViewById(R.id.tv_clock_in_time);

        mTvOther = findViewById(R.id.tv_other);

        showInfo();
    }


    private void showInfo(){
        if (mTvAction!=null && actionRecordEntity!=null){
            int action = actionRecordEntity.getType();
            String actionName = actionRecordEntity.getName();
            if (action == ActionRecordEnum.FOOD.getType() ) {
                actionName = getContext().getString(R.string.bsmonitoring_meal_clock_in);
            } else if (action == ActionRecordEnum.SPORTS.getType() ) {
                actionName = getContext().getString(R.string.common_sports);
            } else if (action == ActionRecordEnum.MEDICATIONS.getType() ) {
                actionName = getContext().getString(R.string.common_medicine);
            } else if (action == ActionRecordEnum.INSULIN.getType()) {
                actionName = getContext().getString(R.string.common_insulin);
            } else if (action == ActionRecordEnum.SLEEP.getType() ) {
                actionName = getContext().getString(R.string.common_sleep);
            } else if (action == ActionRecordEnum.FINGER_BLOOD.getType() ) {
                actionName = getContext().getString(R.string.bsmonitoring_finger_blood);
            } else if (action == ActionRecordEnum.PHYSICAL_STATE.getType() ) {
                actionName = getContext().getString(R.string.common_condition);
            }

            if (ObjectUtils.isNotEmpty(actionName)){
                mTvAction.setText(actionName);
            }
        }

        if (mTvTime!=null && actionRecordEntity!=null){
            long startMill = actionRecordEntity.getStartTime();
            if (startMill!=0){
                mTvTime.setText(TimeUtils.millis2String(startMill,"MM-dd HH:mm"));
            }

        }

        if (mTvOther!=null ){
            if (actionRecordEntity!=null){
                String otherInfo = actionRecordEntity.getEventDetail();
                if (ObjectUtils.isNotEmpty(otherInfo) && actionRecordEntity.getType() == ActionRecordEnum.FINGER_BLOOD.getType()){
                    mTvOther.setVisibility(VISIBLE);
                    if (actionRecordEntity.getType() == ActionRecordEnum.FINGER_BLOOD.getType()) {
                        try {
                            otherInfo = GlucoseUtils.getUserConfigValue(Float.parseFloat(otherInfo), BgUnitUtils.isUserMol());
                        } catch (Exception e){}
                    }
                    mTvOther.setText(otherInfo);
                }else {
                    mTvOther.setVisibility(GONE);
                }
            }else {
                mTvOther.setVisibility(GONE);
            }

        }
    }
}
