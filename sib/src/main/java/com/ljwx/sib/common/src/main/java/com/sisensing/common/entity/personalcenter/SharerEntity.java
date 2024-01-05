package com.sisensing.common.entity.personalcenter;

import com.sisensing.common.entity.alarm.UserGlucoseAlarmSettingsData;

import java.io.Serializable;

public class SharerEntity implements Serializable {


    private AlarmEntity alarm;
    private UserGlucoseAlarmSettingsData newAlarm;
    private String deviceId;
    private String userEmail;
    private long enableTime;
    private String id;
    private long lastTime;
    //最新数据状态(0:平稳 1:缓慢上升 -1:缓慢下降 2:较快上升 -2:较快下降)
    private int latestDataStatus;
    private int latestIndex;
    private long latestTime;
    private float latestValue;
    //设备状态，0：未启用 1：使用中 2：已停用 3：初始化 4：异常 5:异常超过三小时
    private int status;
    //报警状态 1：正常 2：传感器异常 3：温度过高 4：温度过低 5：血糖过高 6：血糖过低
    private int alarmStatus;
    private String userId;
    private String userNickname;

    public AlarmEntity getAlarm() {
        return alarm;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getEnableTime() {
        return enableTime;
    }

    public String getId() {
        return id;
    }

    public long getLastTime() {
        return lastTime;
    }

    public int getLatestDataStatus() {
        return latestDataStatus;
    }

    public int getLatestIndex() {
        return latestIndex;
    }

    public long getLatestTime() {
        return latestTime;
    }

    public float getLatestValue() {
        return latestValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAlarm(AlarmEntity alarm) {
        this.alarm = alarm;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setLatestValue(float latestValue) {
        this.latestValue = latestValue;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public void setLatestTime(long latestTime) {
        this.latestTime = latestTime;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public class AlarmEntity implements Serializable{
        private float lower;
        private float upper;
        private boolean enable;
        private boolean forceRemind;
        private int style;
        private String sound;
        private String alarmInterval;
        private String id;

        public float getUpper() {
            return upper;
        }

        public float getLower() {
            return lower;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAlarmInterval() {
            return alarmInterval;
        }

        public void setAlarmInterval(String alarmInterval) {
            this.alarmInterval = alarmInterval;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public void setUpper(float upper) {
            this.upper = upper;
        }

        public void setLower(float lower) {
            this.lower = lower;
        }

        public boolean isForceRemind() {
            return forceRemind;
        }

        public void setForceRemind(boolean forceRemind) {
            this.forceRemind = forceRemind;
        }
    }

    public UserGlucoseAlarmSettingsData getNewAlarm() {
        return newAlarm;
    }

    public void setNewAlarm(UserGlucoseAlarmSettingsData newAlarm) {
        this.newAlarm = newAlarm;
    }
}
