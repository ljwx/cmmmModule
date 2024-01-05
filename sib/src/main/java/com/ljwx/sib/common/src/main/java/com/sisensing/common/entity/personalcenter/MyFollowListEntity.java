package com.sisensing.common.entity.personalcenter;

import java.util.List;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.personalcenter
 * @Author: l.chenlu
 * @CreateDate: 2021/6/10 16:35
 * @Description:
 */
public class MyFollowListEntity {

    private int currentPage;
    private int pageSize;
    private List<RecordsDTO> records;
    private int total;
    private int totalPage;

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

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public static class RecordsDTO {
        private String followTime;
        private FollowedOtherInfoDTO followedOtherInfo;
        private FollowUserInfoDTO followUserInfo;
        private FollowedDeviceGlucoseDataPODTO followedDeviceGlucoseDataPO;
        private String followedUserId;
        private FollowedUserInfoDTO followedUserInfo;
        private String id;
        private String notes;
        private OtherInfoDTO otherInfo;
        private String source;
        private Integer status;
        private String userId;

        public String getFollowTime() {
            return followTime;
        }

        public void setFollowTime(String followTime) {
            this.followTime = followTime;
        }

        public FollowedOtherInfoDTO getFollowedOtherInfo() {
            return followedOtherInfo;
        }

        public void setFollowedOtherInfo(FollowedOtherInfoDTO followedOtherInfo) {
            this.followedOtherInfo = followedOtherInfo;
        }

        public FollowUserInfoDTO getFollowUserInfo() {
            return followUserInfo;
        }

        public void setFollowUserInfo(FollowUserInfoDTO followUserInfo) {
            this.followUserInfo = followUserInfo;
        }

        public FollowedDeviceGlucoseDataPODTO getFollowedDeviceGlucoseDataPO() {
            return followedDeviceGlucoseDataPO;
        }

        public void setFollowedDeviceGlucoseDataPO(FollowedDeviceGlucoseDataPODTO followedDeviceGlucoseDataPO) {
            this.followedDeviceGlucoseDataPO = followedDeviceGlucoseDataPO;
        }

        public String getFollowedUserId() {
            return followedUserId;
        }

        public void setFollowedUserId(String followedUserId) {
            this.followedUserId = followedUserId;
        }

        public FollowedUserInfoDTO getFollowedUserInfo() {
            return followedUserInfo;
        }

        public void setFollowedUserInfo(FollowedUserInfoDTO followedUserInfo) {
            this.followedUserInfo = followedUserInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public OtherInfoDTO getOtherInfo() {
            return otherInfo;
        }

        public void setOtherInfo(OtherInfoDTO otherInfo) {
            this.otherInfo = otherInfo;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public static class FollowedOtherInfoDTO {
            private String followRemark;

            public String getFollowRemark() {
                return followRemark;
            }

            public void setFollowRemark(String followRemark) {
                this.followRemark = followRemark;
            }
        }

        public static class FollowUserInfoDTO {
            private String avatar;
            private String nickName;
            private String userName;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        public static class FollowedDeviceGlucoseDataPODTO {
            private int bloodGlucoseTrend;
            private String deviceEnableTime;
            private String deviceId;
            private int deviceStatus;
            private GlucoseDataDTO glucoseData;
            private String latestGlucoseTime;
            private float latestGlucoseValue;
            private String userId;

            public int getBloodGlucoseTrend() {
                return bloodGlucoseTrend;
            }

            public void setBloodGlucoseTrend(int bloodGlucoseTrend) {
                this.bloodGlucoseTrend = bloodGlucoseTrend;
            }

            public String getDeviceEnableTime() {
                return deviceEnableTime;
            }

            public void setDeviceEnableTime(String deviceEnableTime) {
                this.deviceEnableTime = deviceEnableTime;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public int getDeviceStatus() {
                return deviceStatus;
            }

            public void setDeviceStatus(int deviceStatus) {
                this.deviceStatus = deviceStatus;
            }

            public GlucoseDataDTO getGlucoseData() {
                return glucoseData;
            }

            public void setGlucoseData(GlucoseDataDTO glucoseData) {
                this.glucoseData = glucoseData;
            }

            public String getLatestGlucoseTime() {
                return latestGlucoseTime;
            }

            public void setLatestGlucoseTime(String latestGlucoseTime) {
                this.latestGlucoseTime = latestGlucoseTime;
            }

            public float getLatestGlucoseValue() {
                return latestGlucoseValue;
            }

            public void setLatestGlucoseValue(float latestGlucoseValue) {
                this.latestGlucoseValue = latestGlucoseValue;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public static class GlucoseDataDTO {
            }
        }

        public static class FollowedUserInfoDTO {
            private String avatar;
            private String nickName;
            private String userName;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        public static class OtherInfoDTO {
            private boolean dataWarn;
            private String dataWarnLower;
            private String dataWarnUpper;
            private boolean warnWayMiniProgram;
            private boolean warnWayOfficialAccount;
            private String followRemark;
            private String followedRemark;
            private String noticeRate;


            public boolean isDataWarn() {
                return dataWarn;
            }

            public void setDataWarn(boolean dataWarn) {
                this.dataWarn = dataWarn;
            }

            public String getDataWarnLower() {
                return dataWarnLower;
            }

            public void setDataWarnLower(String dataWarnLower) {
                this.dataWarnLower = dataWarnLower;
            }

            public String getDataWarnUpper() {
                return dataWarnUpper;
            }

            public void setDataWarnUpper(String dataWarnUpper) {
                this.dataWarnUpper = dataWarnUpper;
            }

            public boolean isWarnWayMiniProgram() {
                return warnWayMiniProgram;
            }

            public void setWarnWayMiniProgram(boolean warnWayMiniProgram) {
                this.warnWayMiniProgram = warnWayMiniProgram;
            }

            public boolean isWarnWayOfficialAccount() {
                return warnWayOfficialAccount;
            }

            public void setWarnWayOfficialAccount(boolean warnWayOfficialAccount) {
                this.warnWayOfficialAccount = warnWayOfficialAccount;
            }

            public String getFollowRemark() {
                return followRemark;
            }

            public void setFollowRemark(String followRemark) {
                this.followRemark = followRemark;
            }

            public String getFollowedRemark() {
                return followedRemark;
            }

            public void setFollowedRemark(String followedRemark) {
                this.followedRemark = followedRemark;
            }

            public String getNoticeRate() {
                return noticeRate;
            }

            public void setNoticeRate(String noticeRate) {
                this.noticeRate = noticeRate;
            }
        }
    }
}
