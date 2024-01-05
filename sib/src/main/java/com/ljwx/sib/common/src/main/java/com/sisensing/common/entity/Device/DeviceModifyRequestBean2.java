package com.sisensing.common.entity.Device;

/**
 * @author y.xie
 * @date 2021/3/17 15:12
 * @desc
 */
public class DeviceModifyRequestBean2 {


    private DeviceAddRequestBean2.AddressDTO address;
    private Integer alarmStatus;
    private String blueToothNum;
    private String createBy;
    private String createTime;
    private String enableTime;
    private String fv;
    private Integer id;
    private String inviteCode;
    private String lastTime;
    private String macAddress;
    private String name;
    private String remark;
    private Integer signDoctorId;
    private Integer status;
    private String updateBy;
    private String updateTime;
    private String userId;
    private String algorithmVersion;

    public DeviceAddRequestBean2.AddressDTO getAddress() {
        return address;
    }

    public void setAddress(DeviceAddRequestBean2.AddressDTO address) {
        this.address = address;
    }

    public Integer getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getBlueToothNum() {
        return blueToothNum;
    }

    public void setBlueToothNum(String blueToothNum) {
        this.blueToothNum = blueToothNum;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getFv() {
        return fv;
    }

    public void setFv(String fv) {
        this.fv = fv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSignDoctorId() {
        return signDoctorId;
    }

    public void setSignDoctorId(Integer signDoctorId) {
        this.signDoctorId = signDoctorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAlgorithmVersion(String algorithmVersion) {
        this.algorithmVersion = algorithmVersion;
    }
}
