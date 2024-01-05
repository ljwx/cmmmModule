package com.sisensing.common.entity.actionRecord;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/10/15 14:56
 * @desc 批量
 */
public class BatchActionData {
    private Long actionStartTime;
    private Long actionEndTime;
    private String deviceId;
    private String dataId;
    private int type;
    private List<ActionData> actionDataList;
    private ActionData actionData;

    public Long  getActionStartTime() {
        return actionStartTime;
    }

    public void setActionStartTime(Long  actionStartTime) {
        this.actionStartTime = actionStartTime;
    }

    public Long  getActionEndTime() {
        return actionEndTime;
    }

    public void setActionEndTime(Long  actionEndTime) {
        this.actionEndTime = actionEndTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ActionData> getActionDataList() {
        return actionDataList;
    }

    public void setActionDataList(List<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public ActionData getActionData() {
        return actionData;
    }

    public void setActionData(ActionData actionData) {
        this.actionData = actionData;
    }

    public static class ActionData{
        private String eventConsume;
        private String eventDetail;
        private String eventType;
        private String unit;

        private String unitType;

        public String getEventConsume() {
            return eventConsume;
        }

        public void setEventConsume(String eventConsume) {
            this.eventConsume = eventConsume;
        }

        public String getEventDetail() {
            return eventDetail;
        }

        public void setEventDetail(String eventDetail) {
            this.eventDetail = eventDetail;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }
    }
}
