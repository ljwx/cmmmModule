package com.sisensing.common.entity.Device;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.Device
 * @Author: f.deng
 * @CreateDate: 2021/3/6 15:00
 * @Description: 复合主键确定唯一性
 */
@Entity(primaryKeys = {"deviceName", "userId"})
public class DeviceEntity implements Parcelable {


    private String macAddress;   //mac地址
    @NotNull
    private String deviceName;    //设备名称

    private int rssi;            //信号强度

    private String blueToothNum; //设备链接码
    @NotNull
    private String userId; //所属患者

    private String deviceId;//设备ID

    private long connectMill;//连接成功后的时间戳

    private long firstBsMill;//第一笔血糖数据的时间戳

    private int deviceStatus;//设备状态 0：未启用(当前没有) 1.使用中 2已停用(失效) 3.初始化 4.异常(传感器损坏)

    private float sensitivity;  //灵敏度

    private int uploadDeviceStatus;//上传设备状态(0.未上传 1.已上传)

    private String algorithmVersion; //当前设备使用的算法版本

    private String algorithmContext;//算法中间变量

    private int algorithmContextIndex;//变量对应的index值

    private String characteristic;//特征信息(例如型号，制造商，序列号等等)


    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getBlueToothNum() {
        return blueToothNum;
    }

    public void setBlueToothNum(String blueToothNum) {
        this.blueToothNum = blueToothNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getConnectMill() {
        return connectMill;
    }

    public void setConnectMill(long connectMill) {
        this.connectMill = connectMill;
    }

    public long getFirstBsMill() {
        return firstBsMill;
    }

    public void setFirstBsMill(long firstBsMill) {
        this.firstBsMill = firstBsMill;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getUploadDeviceStatus() {
        return uploadDeviceStatus;
    }

    public void setUploadDeviceStatus(int uploadDeviceStatus) {
        this.uploadDeviceStatus = uploadDeviceStatus;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }

    public String getAlgorithmContext() {
        return algorithmContext;
    }

    public void setAlgorithmContext(String algorithmContext) {
        this.algorithmContext = algorithmContext;
    }


    public int getAlgorithmContextIndex() {
        return algorithmContextIndex;
    }

    public void setAlgorithmContextIndex(int algorithmContextIndex) {
        this.algorithmContextIndex = algorithmContextIndex;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public DeviceEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.macAddress);
        dest.writeString(this.deviceName);
        dest.writeInt(this.rssi);
        dest.writeString(this.blueToothNum);
        dest.writeString(this.userId);
        dest.writeString(this.deviceId);
        dest.writeLong(this.connectMill);
        dest.writeLong(this.firstBsMill);
        dest.writeInt(this.deviceStatus);
        dest.writeFloat(this.sensitivity);
        dest.writeInt(this.uploadDeviceStatus);
        dest.writeString(this.algorithmVersion);
        dest.writeString(this.algorithmContext);
        dest.writeInt(this.algorithmContextIndex);
    }

    protected DeviceEntity(Parcel in) {
        this.macAddress = in.readString();
        this.deviceName = in.readString();
        this.rssi = in.readInt();
        this.blueToothNum = in.readString();
        this.userId = in.readString();
        this.deviceId = in.readString();
        this.connectMill = in.readLong();
        this.firstBsMill = in.readLong();
        this.deviceStatus = in.readInt();
        this.sensitivity = in.readFloat();
        this.uploadDeviceStatus = in.readInt();
        this.algorithmVersion = in.readString();
        this.algorithmContext = in.readString();
        this.algorithmContextIndex = in.readInt();
    }

    public static final Creator<DeviceEntity> CREATOR = new Creator<DeviceEntity>() {
        @Override
        public DeviceEntity createFromParcel(Parcel source) {
            return new DeviceEntity(source);
        }

        @Override
        public DeviceEntity[] newArray(int size) {
            return new DeviceEntity[size];
        }
    };
}
