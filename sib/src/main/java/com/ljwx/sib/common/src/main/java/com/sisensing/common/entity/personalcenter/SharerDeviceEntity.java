package com.sisensing.common.entity.personalcenter;

import java.util.List;

public class SharerDeviceEntity {

    private String userId;
    private String userNickName;
    private String deviceId;
    private String deviceName;
    private String blueToothNum;
    private long deviceEnableTime;
    //设备状态（0：未启用 1：使用中 2：已停用 3：初始化 4：异常 5:异常超过三小时）
    private int deviceStatus;
    //报警状态（1：正常 2：传感器异常 3：温度过高 4：温度过低 5：血糖过高 6：血糖过低）
    private int deviceAlarmStatus;
    private int latestIndex;
    private float latestGlucoseValue;
    private int bloodGlucoseTrend;
    private long latestGlucoseTime;
    private long deviceLastTime;
    private List<GlucoseInfo> glucoseInfos;
    private Target target;

    public String getUserId() {
        return userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getBlueToothNum() {
        return blueToothNum;
    }

    public long getDeviceEnableTime() {
        return deviceEnableTime;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public int getDeviceAlarmStatus() {
        return deviceAlarmStatus;
    }

    public int getLatestIndex() {
        return latestIndex;
    }

    public float getLatestGlucoseValue() {
        return latestGlucoseValue;
    }

    public int getBloodGlucoseTrend() {
        return bloodGlucoseTrend;
    }

    public long getLatestGlucoseTime() {
        return latestGlucoseTime;
    }

    public long getDeviceLastTime() {
        return deviceLastTime;
    }

    public List<GlucoseInfo> getGlucoseInfos() {
        return glucoseInfos;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public class GlucoseInfo{
        private long t; //时间戳
        private float v;    //血糖值
        private int i;  //索引值
        private int s;  //	变化趋势 0:平稳 1:缓慢上升 -1:缓慢下降 2:较快上升 -2:较快下降
        private int ast;  //	报警状态 1：正常 2：传感器异常 3：温度过高 4：温度过低 5：血糖过高 6：血糖过低

        public long getT() {
            return t;
        }

        public void setT(long t) {
            this.t = t;
        }

        public float getV() {
            return v;
        }

        public void setV(float v) {
            this.v = v;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }

        public int getAst() {
            return ast;
        }

        public void setAst(int ast) {
            this.ast = ast;
        }
    }

    public class Target{
        private float upper;
        private float lower;

        public float getUpper() {
            return upper;
        }

        public void setUpper(float upper) {
            this.upper = upper;
        }

        public float getLower() {
            return lower;
        }

        public void setLower(float lower) {
            this.lower = lower;
        }
    }
}
