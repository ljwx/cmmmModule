package com.sisensing.common.entity.BloodGlucoseEntity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.BloodGlucoseEntity
 * @Author: f.deng
 * @CreateDate: 2021/3/6 15:00
 * @Description:
 */
@Entity
public class BloodGlucoseEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int index = 0;                    //索引值，对应血糖数据的第几笔  greenDao的主键只能用Long型的来表达
    private int validIndex;                     //5min一笔数据，默认值为0
    private String macAddress;                 //mac地址
    private String bleName;                    //蓝牙名称
    private float temperatureValue;            //温度值
    private float glucoseValue;                //血糖值（算法处理后的值）
    private float currentValue;                //电流值
    private long processedTimeMill;            //当前血糖计算的时间戳
    private int temperatureWarning = 0;        //温度报警 0.正常  1.过低  2.过高
    private int currentWarning = 0;            //电流报警 0.正常  1.过低  2.过高
    private int glucoseWarning = 0;            //血糖报警  0.正常  1.过低  2.过高
    private int glucoseTrend = 0;               //0.平稳  1.缓慢上升  -1.缓慢下降  2.较快上升  -2.较快下降
    private int stateValue;                     //状态值 当检测到该值为 6时，做出重启提示
    private double sensitivity;                 //灵敏度
    private String userId;
    private int numOfUnreceived;                //未发送笔数
    private int alarmStatus = 0;                  //1.正常 2.半小时的电流异常 3.温度过高 4.温度过低 5.血糖过高 6.血糖过低
    private long electric = 0;                  //电量值

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValidIndex() {
        return validIndex;
    }

    public void setValidIndex(int validIndex) {
        this.validIndex = validIndex;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public long getElectric() {
        return electric;
    }

    public void setElectric(long electric) {
        this.electric = electric;
    }

    public double getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(double sens) {
        sensitivity = sens;
    }

    public float getGlucoseValue() {
        return glucoseValue;
    }

    public void setGlucoseValue(float glucoseValue) {
        this.glucoseValue = glucoseValue;
    }

    public int getStateValue() {
        return stateValue;
    }

    public void setStateValue(int stateValue) {
        this.stateValue = stateValue;
    }

    public int getNumOfUnreceived() {
        return numOfUnreceived;
    }

    public void setNumOfUnreceived(int numOfUnreceived) {
        this.numOfUnreceived = numOfUnreceived;
    }

    public int getGlucoseWarning() {
        return glucoseWarning;
    }

    public void setGlucoseWarning(int glucoseWarning) {
        this.glucoseWarning = glucoseWarning;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }


    public long getProcessedTimeMill() {
        return processedTimeMill;
    }

    public void setProcessedTimeMill(long processedTimeMill) {
        this.processedTimeMill = processedTimeMill;
    }


    public int getTemperatureWarning() {
        return temperatureWarning;
    }

    public void setTemperatureWarning(int temperatureWarning) {
        this.temperatureWarning = temperatureWarning;
    }

    public int getCurrentWarning() {
        return currentWarning;
    }

    public void setCurrentWarning(int currentWarning) {
        this.currentWarning = currentWarning;
    }


    public int getGlucoseTrend() {
        return glucoseTrend;
    }

    public void setGlucoseTrend(int glucoseTrend) {
        this.glucoseTrend = glucoseTrend;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeInt(this.validIndex);
        dest.writeString(this.macAddress);
        dest.writeString(this.bleName);
        dest.writeFloat(this.temperatureValue);
        dest.writeFloat(this.glucoseValue);
        dest.writeFloat(this.currentValue);
        dest.writeLong(this.processedTimeMill);
        dest.writeInt(this.temperatureWarning);
        dest.writeInt(this.currentWarning);
        dest.writeInt(this.glucoseTrend);
        dest.writeInt(this.alarmStatus);
    }

    public BloodGlucoseEntity() {
    }

    protected BloodGlucoseEntity(Parcel in) {
        this.index = in.readInt();
        this.validIndex = in.readInt();
        this.macAddress = in.readString();
        this.bleName = in.readString();
        this.temperatureValue = in.readFloat();
        this.glucoseValue = in.readFloat();
        this.currentValue = in.readFloat();
        this.processedTimeMill = in.readLong();
        this.temperatureWarning = in.readInt();
        this.currentWarning = in.readInt();
        this.glucoseTrend = in.readInt();
        this.alarmStatus = in.readInt();
    }

    public static final Creator<BloodGlucoseEntity> CREATOR = new Creator<BloodGlucoseEntity>() {
        @Override
        public BloodGlucoseEntity createFromParcel(Parcel source) {
            return new BloodGlucoseEntity(source);
        }

        @Override
        public BloodGlucoseEntity[] newArray(int size) {
            return new BloodGlucoseEntity[size];
        }
    };
}
