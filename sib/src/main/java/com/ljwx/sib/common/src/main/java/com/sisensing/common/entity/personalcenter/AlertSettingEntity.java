package com.sisensing.common.entity.personalcenter;

public class AlertSettingEntity {
    private float lower;
    private float upper;
    private boolean enable;
    private int style;
    private String sound;
    private String alarmInterval;
    private String id;

    private boolean forceRemind;

    public float getLower() {
        return lower;
    }

    public void setLower(float lower) {
        this.lower = lower;
    }

    public float getUpper() {
        return upper;
    }

    public void setUpper(float upper) {
        this.upper = upper;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(String alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isForceRemind() {
        return forceRemind;
    }

    public void setForceRemind(boolean forceRemind) {
        this.forceRemind = forceRemind;
    }
}
