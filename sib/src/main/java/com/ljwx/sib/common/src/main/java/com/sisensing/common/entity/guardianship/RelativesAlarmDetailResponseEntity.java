package com.sisensing.common.entity.guardianship;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ProjectName: 硅基动感
 * @Package: com.sisensing.common.entity.guardianship
 * @ClassName: RelativesAlarmDetailReponseEntity
 * @Description: 亲友报警详情
 * @Author: xy
 * @Date: 2023/3/9 18:06
 */
public class RelativesAlarmDetailResponseEntity implements Parcelable{

    private List<AlarmSceneVOListDTO> alarmSceneVOList;
    private String followId;
    private OtherInfoDTO otherInfo;

    protected RelativesAlarmDetailResponseEntity(Parcel in) {
        alarmSceneVOList = in.createTypedArrayList(AlarmSceneVOListDTO.CREATOR);
        followId = in.readString();
        otherInfo = in.readParcelable(OtherInfoDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(alarmSceneVOList);
        dest.writeString(followId);
        dest.writeParcelable(otherInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RelativesAlarmDetailResponseEntity> CREATOR = new Creator<RelativesAlarmDetailResponseEntity>() {
        @Override
        public RelativesAlarmDetailResponseEntity createFromParcel(Parcel in) {
            return new RelativesAlarmDetailResponseEntity(in);
        }

        @Override
        public RelativesAlarmDetailResponseEntity[] newArray(int size) {
            return new RelativesAlarmDetailResponseEntity[size];
        }
    };

    public List<AlarmSceneVOListDTO> getAlarmSceneVOList() {
        return alarmSceneVOList;
    }

    public void setAlarmSceneVOList(List<AlarmSceneVOListDTO> alarmSceneVOList) {
        this.alarmSceneVOList = alarmSceneVOList;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public OtherInfoDTO getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(OtherInfoDTO otherInfo) {
        this.otherInfo = otherInfo;
    }

    public static class OtherInfoDTO implements Parcelable{
        private boolean dataSyncEnable;
        private int dataSyncNoticeRate;
        private String dataSyncNoticeSound;
        private String dataWarnLower;
        private String dataWarnUpper;
        private boolean datawarnEnable;
        private String followedRemark;
        private boolean mobilePush;
        private int noticeRate;
        private boolean warnWayOfficialAccount;
        private boolean deviceStatusEnable;

        protected OtherInfoDTO(Parcel in) {
            dataSyncEnable = in.readByte() != 0;
            dataSyncNoticeRate = in.readInt();
            dataSyncNoticeSound = in.readString();
            dataWarnLower = in.readString();
            dataWarnUpper = in.readString();
            datawarnEnable = in.readByte() != 0;
            followedRemark = in.readString();
            mobilePush = in.readByte() != 0;
            noticeRate = in.readInt();
            warnWayOfficialAccount = in.readByte() != 0;
            deviceStatusEnable = in.readByte() != 0;
        }

        public static final Creator<OtherInfoDTO> CREATOR = new Creator<OtherInfoDTO>() {
            @Override
            public OtherInfoDTO createFromParcel(Parcel in) {
                return new OtherInfoDTO(in);
            }

            @Override
            public OtherInfoDTO[] newArray(int size) {
                return new OtherInfoDTO[size];
            }
        };

        public boolean isDataSyncEnable() {
            return dataSyncEnable;
        }

        public void setDataSyncEnable(boolean dataSyncEnable) {
            this.dataSyncEnable = dataSyncEnable;
        }

        public int getDataSyncNoticeRate() {
            return dataSyncNoticeRate;
        }

        public void setDataSyncNoticeRate(int dataSyncNoticeRate) {
            this.dataSyncNoticeRate = dataSyncNoticeRate;
        }

        public String getDataSyncNoticeSound() {
            return dataSyncNoticeSound;
        }

        public void setDataSyncNoticeSound(String dataSyncNoticeSound) {
            this.dataSyncNoticeSound = dataSyncNoticeSound;
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

        public boolean isDatawarnEnable() {
            return datawarnEnable;
        }

        public void setDatawarnEnable(boolean datawarnEnable) {
            this.datawarnEnable = datawarnEnable;
        }

        public String getFollowedRemark() {
            return followedRemark;
        }

        public void setFollowedRemark(String followedRemark) {
            this.followedRemark = followedRemark;
        }

        public boolean isMobilePush() {
            return mobilePush;
        }

        public void setMobilePush(boolean mobilePush) {
            this.mobilePush = mobilePush;
        }

        public int getNoticeRate() {
            return noticeRate;
        }

        public void setNoticeRate(int noticeRate) {
            this.noticeRate = noticeRate;
        }

        public boolean isWarnWayOfficialAccount() {
            return warnWayOfficialAccount;
        }

        public void setWarnWayOfficialAccount(boolean warnWayOfficialAccount) {
            this.warnWayOfficialAccount = warnWayOfficialAccount;
        }

        public boolean isDeviceStatusEnable() {
            return deviceStatusEnable;
        }

        public void setDeviceStatusEnable(boolean deviceStatusEnable) {
            this.deviceStatusEnable = deviceStatusEnable;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (dataSyncEnable ? 1 : 0));
            dest.writeInt(dataSyncNoticeRate);
            dest.writeString(dataSyncNoticeSound);
            dest.writeString(dataWarnLower);
            dest.writeString(dataWarnUpper);
            dest.writeByte((byte) (datawarnEnable ? 1 : 0));
            dest.writeString(followedRemark);
            dest.writeByte((byte) (mobilePush ? 1 : 0));
            dest.writeInt(noticeRate);
            dest.writeByte((byte) (warnWayOfficialAccount ? 1 : 0));
            dest.writeByte((byte) (deviceStatusEnable ? 1 : 0));
        }
    }

    public static class AlarmSceneVOListDTO implements Parcelable {
        private String dataWarnLower;
        private String dataWarnUpper;
        private long effectiveEndTime;
        private long effectiveStartTime;
        private boolean enableStatus;
        private String followId;
        private String id;
        private int retryNum;
        private String ringtone;
        private String sceneName;
        private int sceneType;

        public AlarmSceneVOListDTO() {
        }

        protected AlarmSceneVOListDTO(Parcel in) {
            dataWarnLower = in.readString();
            dataWarnUpper = in.readString();
            effectiveEndTime = in.readLong();
            effectiveStartTime = in.readLong();
            enableStatus = in.readByte() != 0;
            followId = in.readString();
            id = in.readString();
            retryNum = in.readInt();
            ringtone = in.readString();
            sceneName = in.readString();
            sceneType = in.readInt();
        }

        public static final Creator<AlarmSceneVOListDTO> CREATOR = new Creator<AlarmSceneVOListDTO>() {
            @Override
            public AlarmSceneVOListDTO createFromParcel(Parcel in) {
                return new AlarmSceneVOListDTO(in);
            }

            @Override
            public AlarmSceneVOListDTO[] newArray(int size) {
                return new AlarmSceneVOListDTO[size];
            }
        };

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

        public long getEffectiveEndTime() {
            return effectiveEndTime;
        }

        public void setEffectiveEndTime(long effectiveEndTime) {
            this.effectiveEndTime = effectiveEndTime;
        }

        public long getEffectiveStartTime() {
            return effectiveStartTime;
        }

        public void setEffectiveStartTime(long effectiveStartTime) {
            this.effectiveStartTime = effectiveStartTime;
        }

        public boolean isEnableStatus() {
            return enableStatus;
        }

        public void setEnableStatus(boolean enableStatus) {
            this.enableStatus = enableStatus;
        }

        public String getFollowId() {
            return followId;
        }

        public void setFollowId(String followId) {
            this.followId = followId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getRetryNum() {
            return retryNum;
        }

        public void setRetryNum(int retryNum) {
            this.retryNum = retryNum;
        }

        public String getRingtone() {
            return ringtone;
        }

        public void setRingtone(String ringtone) {
            this.ringtone = ringtone;
        }

        public String getSceneName() {
            return sceneName;
        }

        public void setSceneName(String sceneName) {
            this.sceneName = sceneName;
        }

        public int getSceneType() {
            return sceneType;
        }

        public void setSceneType(int sceneType) {
            this.sceneType = sceneType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(dataWarnLower);
            dest.writeString(dataWarnUpper);
            dest.writeLong(effectiveEndTime);
            dest.writeLong(effectiveStartTime);
            dest.writeByte((byte) (enableStatus ? 1 : 0));
            dest.writeString(followId);
            dest.writeString(id);
            dest.writeInt(retryNum);
            dest.writeString(ringtone);
            dest.writeString(sceneName);
            dest.writeInt(sceneType);
        }
    }
}
