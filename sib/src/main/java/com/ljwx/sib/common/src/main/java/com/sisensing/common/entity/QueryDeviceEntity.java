package com.sisensing.common.entity;

import com.sisensing.common.entity.Device.DeviceAddRequestBean;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/8/30 17:08
 * @desc
 */
public class QueryDeviceEntity {

    private Integer currentPage;
    private Integer pageSize;
    private List<RecordsDTO> records;
    private Integer total;
    private Integer totalPage;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public static class RecordsDTO {
        private DeviceAddRequestBean.AddressDTO address;
        private Integer alarmStatus;
        private String algorithmVersion;
        private String blueToothNum;
        private String createBy;
        private long createTime;
        private long enableTime;
        private String fv;
        private String id;
        private String inviteCode;
        private long lastTime;
        private String macAddress;
        private String name;
        private String remark;
        private String signDoctorId;
        private Integer status;
        private String updateBy;
        private long updateTime;
        private String userId;

        public DeviceAddRequestBean.AddressDTO getAddress() {
            return address;
        }

        public void setAddress(DeviceAddRequestBean.AddressDTO address) {
            this.address = address;
        }

        public Integer getAlarmStatus() {
            return alarmStatus;
        }

        public void setAlarmStatus(Integer alarmStatus) {
            this.alarmStatus = alarmStatus;
        }

        public String getAlgorithmVersion() {
            return algorithmVersion;
        }

        public void setAlgorithmVersion(String algorithmVersion) {
            this.algorithmVersion = algorithmVersion;
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getEnableTime() {
            return enableTime;
        }

        public void setEnableTime(long enableTime) {
            this.enableTime = enableTime;
        }

        public String getFv() {
            return fv;
        }

        public void setFv(String fv) {
            this.fv = fv;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
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

        public String getSignDoctorId() {
            return signDoctorId;
        }

        public void setSignDoctorId(String signDoctorId) {
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

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
