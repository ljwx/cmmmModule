package com.sisensing.common.entity.personalcenter;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.ljwx.baseapp.extensions.StringKt;
import com.sisensing.common.R;
import com.sisensing.common.entity.actionRecord.ActionRecordEnum;
import com.sisensing.common.entity.clcok.CheckInData;
import com.sisensing.common.utils.BgUnitUtils;
import com.sisensing.common.utils.GlucoseUtils;
import com.sisensing.common.utils.Log;

import java.util.List;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.personalcenter
 * @Author: l.chenlu
 * @CreateDate: 2021/5/29 14:56
 * @Description:
 */
public class LifeEventEntity {
    private List<LifeEventEntity.RecordsDTO> records;
    private int currentPage;
    private int pageSize;
    private int totalPage;
    private int total;

    public List<LifeEventEntity.RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<LifeEventEntity.RecordsDTO> records) {
        this.records = records;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class RecordsDTO {
        private String id;
        private String createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private String userId;
        private int type;
        private LifeEventEntity.RecordsDTO.ActionDataDTO actionData;
        private long actionStartTime;
        private long actionEndTime;
        private String deviceId;
        private String dataId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getCreateTimeDisplay() {
            return TimeUtils.millis2String(actionStartTime, "HH:mm");
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getType() {
            return type;
        }

        public boolean showSportIcon() {
            String time =  changeTime((actionEndTime - actionStartTime)/1000);
            if (type == ActionRecordEnum.SPORTS.getType() && ObjectUtils.isNotEmpty(time)) {
                return true;
            }
            return false;
        }

        public String getTypeDisplay() {
            if (type == ActionRecordEnum.FOOD.getType()) {
                return StringUtils.getString(R.string.bsmonitoring_meal_clock_in);
            } else if (type == ActionRecordEnum.SPORTS.getType()) {
                return StringUtils.getString(R.string.common_sports);
            } else if (type == ActionRecordEnum.MEDICATIONS.getType()) {
                return StringUtils.getString(R.string.common_medicine);
            } else if (type == ActionRecordEnum.INSULIN.getType()) {
                return StringUtils.getString(R.string.common_insulin);
            } else if (type == ActionRecordEnum.SLEEP.getType()) {
                return StringUtils.getString(R.string.common_sleep);
            } else if (type == ActionRecordEnum.FINGER_BLOOD.getType()) {
                return StringUtils.getString(R.string.bsmonitoring_finger_blood);
            } else if (type == ActionRecordEnum.PHYSICAL_STATE.getType()) {
                return StringUtils.getString(R.string.common_condition);
            } else {
                return "";
            }
        }

        public void setType(int type) {
            this.type = type;
        }

        public LifeEventEntity.RecordsDTO.ActionDataDTO getActionData() {
            return actionData;
        }

        public void setActionData(LifeEventEntity.RecordsDTO.ActionDataDTO actionData) {
            this.actionData = actionData;
        }

        public long getActionStartTime() {
            return actionStartTime;
        }

        public void setActionStartTime(long actionStartTime) {
            this.actionStartTime = actionStartTime;
        }

        public long getActionEndTime() {
            return actionEndTime;
        }

        public void setActionEndTime(long actionEndTime) {
            this.actionEndTime = actionEndTime;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }

        public Drawable getDrawable() {
            if (getType() == CheckInData.Companion.getCHECKIN_SPORTS()) {
                return ContextCompat.getDrawable(Utils.getApp(), R.mipmap.personalcenter_duration);
            } else {
                return null;
            }
        }

        public String getShowDetail() {
            StringBuilder builder = new StringBuilder();
            String unit = "";
            String mid = "";
            int type = getType();
            if (type == CheckInData.Companion.getCHECKIN_MEAL() && ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                unit = StringUtils.getString(R.string.unit_weight_g);
            }
            if (type == CheckInData.Companion.getCHECKIN_SPORTS()) {
                mid =  changeTime((actionEndTime - actionStartTime)/1000);
                if (ObjectUtils.isNotEmpty(actionData.eventDetail)) {
                    String m = ObjectUtils.isEmpty(mid) ? "" : ",";
                    return mid + m + actionData.eventDetail;
                }
                return mid;
            }
            if (type == CheckInData.Companion.getCHECKIN_MEDICATIONS() && ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                unit = " " + StringUtils.getString(R.string.personalcenter_bs_unit);
            }
            if (type == CheckInData.Companion.getCHECKIN_INSULIN() && ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                unit = " " + StringUtils.getString(R.string.personalcenter_bs_unit);
            }
            if (type == CheckInData.Companion.getCHECKID_FINGER_BLOOD()) {
//                unit =  BgUnitUtils.isTypeMol(actionData.getUnit()) ? BgUnitUtils.getMolUnit() : BgUnitUtils.getMgUnit();
                unit =  BgUnitUtils.getUserUnit();
            }
            if (type == CheckInData.Companion.getCHECKIN_SLEEP()) {
                return changeTime((actionEndTime - actionStartTime)/1000);
            }
            if (ObjectUtils.isNotEmpty(actionData.eventDetail)) {
                if (type == CheckInData.Companion.getCHECKID_FINGER_BLOOD()) {
                    if (ObjectUtils.isNotEmpty(actionData.eventDetail)) {
                        if (StringKt.isInt(actionData.eventDetail) || StringKt.isFloat(actionData.eventDetail)) {
                            builder.append(GlucoseUtils.getConvertValue(Float.parseFloat(actionData.eventDetail), BgUnitUtils.isTypeMol(actionData.getUnit()), BgUnitUtils.isUserMol()));
                        }
                    }
                } else {
                    builder.append(actionData.eventDetail);
                }
                builder.append(mid);
            }
            if (ObjectUtils.isNotEmpty(actionData.eventDetail) && ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                builder.append(",");
            }
            if (ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                builder.append(actionData.eventConsume);
            }
            if (ObjectUtils.isNotEmpty(actionData.eventDetail) || ObjectUtils.isNotEmpty(actionData.eventConsume)) {
                builder.append(unit);
            }
            return builder.toString();
        }

        /**
         *
         * @param time 秒
         * @return
         */
        private String changeTime(long time){
            long d = time / (60 * 60 * 24);

            long h = (time - (60 * 60 * 24 * d)) / 3600;

            long m = (time - 60 * 60 * 24 * d - 3600 * h) / 60;

            long s = time - 60 * 60 * 24 * d - 3600 * h - 60 * m;

            StringBuilder builder = new StringBuilder();
            if(d>0){
                builder.append(d+StringUtils.getString(R.string.day));
            }
            if(h>0){
                builder.append(h+StringUtils.getString(R.string.dailybs_hour_h));
            }
            if(m>0){
                builder.append(m+ StringUtils.getString(R.string.min));
            }
            return builder.toString();
        }

        public static class ActionDataDTO {
            private String name;
            private long startTime;
            private List<String> actionImgs;
            private long endTime;
            private String eventType;
            private String eventConsume;
//            private int type;
            private String userId;
            private String eventDetail;
            private String unit;

            public String getEventDetail() {
                return eventDetail;
            }

            public void setEventDetail(String eventDetail) {
                this.eventDetail = eventDetail;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public List<String> getActionImgs() {
                return actionImgs;
            }

            public void setActionImgs(List<String> actionImgs) {
                this.actionImgs = actionImgs;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public String getEventType() {
                return eventType;
            }

            public void setEventType(String eventType) {
                this.eventType = eventType;
            }

            public String getEventConsume() {
                return eventConsume;
            }

            public void setEventConsume(String eventConsume) {
                this.eventConsume = eventConsume;
            }

//            public int getType() {
//                return type;
//            }

//            public void setType(int type) {
//                this.type = type;
//            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
    }
}
