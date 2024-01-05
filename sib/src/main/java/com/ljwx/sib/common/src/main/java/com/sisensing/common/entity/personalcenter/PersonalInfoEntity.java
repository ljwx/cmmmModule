package com.sisensing.common.entity.personalcenter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author y.xie
 * @date 2021/5/17 15:45
 * @desc 个人中心-个人信息
 */
public class PersonalInfoEntity implements Parcelable {

    /**
     * 注册未设置血糖提醒时的response
     * "data": {
     *    "id": "1719293815459812302",
     *    "emptyPassword": false,
     *    "email": "testalarm@sf.cn",
     *    "sex": null,
     *    "countryName": null,
     *    "birthday": "1377878400000",
     *    "loginIp": "10.244.2.1",
     *    "loginTime": "1698746582051",
     *    "nickName": "alarm",
     *    "drType": 0,
     *    "target": {
     *       "upper": 140.5,
     *       "lower": 70.3
     *    },
     *    "alarm": {
     *       "enable": false,
     *       "style": 0,
     *       "upper": 0,
     *       "lower": 0,
     *       "sound": null,
     *       "alarmInterval": 5,
     *       "forceRemind": false
     *    },
     *    "condition": 0,
     *    "countryCode": null,
     *    "language": null,
     *    "glucoseUnit": 1
     * },
     */

    private AlarmDTO alarm;
    private String email;
    private String avatar;
    private String birthday;
    private String courseOfDisease;
    private String height;
    private String id;
    private String loginIp;
    private long loginTime;
    private MealsInfoDTO mealsInfo;
    private String phone;
    private String sex;
    private TargetDTO target;
    private TreatmentInfoDTO treatmentInfo;
    private String userAccount;
    private String userName;
    private String weight;
    private WorkRestInfoDTO workRestInfo;

    private Integer alarmInterval;

    /**
     * 糖尿病类型扩展列(0：none of all；1：pregnancy；2，older；3，high-risk)
     */
    private int condition;
    /**
     * 糖尿病类型( 0: undiagnosed；1: Type1；2: Type2；7：others)
     */
    private int drType;
    /**
     * 血糖目标下限一级
     */
    private Float lower_1;
    /**
     * 血糖报警下限低
     */
    private Float lower_l;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 血糖目标上限一级
     */
    private Float upper_1;
    /**
     * 血糖报警上限低
     */
    private Float upper_l;

    private int glucoseUnit; //1:mmol/L 2:mg/dL

    private String countryCode;

    private String countryName;

    private boolean boundFlag; //用户是否绑定过设备

    private int latestDeviceStatus; //最新设备状态

    public PersonalInfoEntity() {
    }

    protected PersonalInfoEntity(Parcel in) {
        avatar = in.readString();
        birthday = in.readString();
        courseOfDisease = in.readString();
        drType = in.readInt();
        glucoseUnit = in.readInt();
        condition = in.readInt();
        height = in.readString();
        id = in.readString();
        loginIp = in.readString();
        loginTime = in.readLong();
        nickName = in.readString();
        phone = in.readString();
        sex = in.readString();
        userAccount = in.readString();
        userName = in.readString();
        countryCode = in.readString();
        countryName = in.readString();
        weight = in.readString();
        alarmInterval = in.readInt();
        lower_1 = in.readFloat();
        lower_l = in.readFloat();
        upper_1 = in.readFloat();
        upper_l = in.readFloat();
        boundFlag = in.readBoolean();
        latestDeviceStatus = in.readInt();
    }

    public static final Creator<PersonalInfoEntity> CREATOR = new Creator<PersonalInfoEntity>() {
        @Override
        public PersonalInfoEntity createFromParcel(Parcel in) {
            return new PersonalInfoEntity(in);
        }

        @Override
        public PersonalInfoEntity[] newArray(int size) {
            return new PersonalInfoEntity[size];
        }
    };

    public AlarmDTO getAlarm() {
        return alarm;
    }

    public void setAlarm(AlarmDTO alarm) {
        this.alarm = alarm;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCourseOfDisease() {
        return courseOfDisease;
    }

    public void setCourseOfDisease(String courseOfDisease) {
        this.courseOfDisease = courseOfDisease;
    }

    public int getDrType() {
        return drType;
    }

    public void setDrType(int drType) {
        this.drType = drType;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public MealsInfoDTO getMealsInfo() {
        return mealsInfo;
    }

    public void setMealsInfo(MealsInfoDTO mealsInfo) {
        this.mealsInfo = mealsInfo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public TargetDTO getTarget() {
        return target;
    }

    public void setTarget(TargetDTO target) {
        this.target = target;
    }

    public TreatmentInfoDTO getTreatmentInfo() {
        return treatmentInfo;
    }

    public void setTreatmentInfo(TreatmentInfoDTO treatmentInfo) {
        this.treatmentInfo = treatmentInfo;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public WorkRestInfoDTO getWorkRestInfo() {
        return workRestInfo;
    }

    public void setWorkRestInfo(WorkRestInfoDTO workRestInfo) {
        this.workRestInfo = workRestInfo;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public Float getLower_1() {
        return lower_1;
    }

    public void setLower_1(Float lower_1) {
        this.lower_1 = lower_1;
    }

    public Float getLower_l() {
        return lower_l;
    }

    public void setLower_l(Float lower_l) {
        this.lower_l = lower_l;
    }

    public Float getUpper_1() {
        return upper_1;
    }

    public void setUpper_1(Float upper_1) {
        this.upper_1 = upper_1;
    }

    public Float getUpper_l() {
        return upper_l;
    }

    public void setUpper_l(Float upper_l) {
        this.upper_l = upper_l;
    }

    public int getGlucoseUnit() {
        return glucoseUnit;
    }

    public void setGlucoseUnit(int glucoseUnit) {
        this.glucoseUnit = glucoseUnit;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(Integer alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public boolean isBoundFlag() {
        return boundFlag;
    }

    public void setBoundFlag(boolean boundFlag) {
        this.boundFlag = boundFlag;
    }

    public int getLatestDeviceStatus() {
        return latestDeviceStatus;
    }

    public void setLatestDeviceStatus(int latestDeviceStatus) {
        this.latestDeviceStatus = latestDeviceStatus;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(birthday);
        dest.writeString(courseOfDisease);
        dest.writeInt(drType);
        dest.writeInt(glucoseUnit);
        dest.writeInt(condition);
        dest.writeString(height);
        dest.writeString(id);
        dest.writeString(loginIp);
        dest.writeLong(loginTime);
        dest.writeString(nickName);
        dest.writeString(countryCode);
        dest.writeString(countryName);
        dest.writeString(phone);
        dest.writeString(sex);
        dest.writeString(userAccount);
        dest.writeString(userName);
        dest.writeString(weight);
        dest.writeInt(alarmInterval);
        dest.writeFloat(lower_l);
        dest.writeFloat(lower_1);
        dest.writeFloat(upper_l);
        dest.writeFloat(upper_1);
        dest.writeBoolean(boundFlag);
        dest.writeInt(latestDeviceStatus);
    }

    public String getEmail() {
        return email;
    }

    public static class AlarmDTO {

        private float lower;
        private float upper;
        private boolean enable;
        private boolean forceRemind;
        private int style;
        private String sound;
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

        public boolean isForceRemind() {
            return forceRemind;
        }

        public void setForceRemind(boolean forceRemind) {
            this.forceRemind = forceRemind;
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

    }

    public static class MealsInfoDTO {
        private String breakfastTime;
        private String dinnerTime;
        private String lunchTime;

        public String getBreakfastTime() {
            return breakfastTime;
        }

        public void setBreakfastTime(String breakfastTime) {
            this.breakfastTime = breakfastTime;
        }

        public String getDinnerTime() {
            return dinnerTime;
        }

        public void setDinnerTime(String dinnerTime) {
            this.dinnerTime = dinnerTime;
        }

        public String getLunchTime() {
            return lunchTime;
        }

        public void setLunchTime(String lunchTime) {
            this.lunchTime = lunchTime;
        }
    }

    public static class TargetDTO {
        private float lower_1;
        private float lower_2;
        private float upper_1;

        private float upper;

        private float lower;
        private float upper_2;

        private int isRec = 1;//是否推荐

        public float getLower_1() {
            return lower;
        }

        public void setLower_1(float lower_1) {
            this.lower = lower_1;
        }

        public float getLower_2() {
            return lower_2;
        }

        public void setLower_2(float lower_2) {
            this.lower_2 = lower_2;
        }

        public float getUpper_1() {
            return upper;
        }

        public void setUpper_1(float upper_1) {
            this.upper = upper_1;
        }

        public float getUpper_2() {
            return upper_2;
        }

        public void setUpper_2(float upper_2) {
            this.upper_2 = upper_2;
        }

        public float getUpper() {
            return upper;
        }

        public float getLower() {
            return lower;
        }

        public void setLower(float lower) {
            this.lower = lower;
        }

        public void setUpper(float upper) {
            this.upper = upper;
        }
    }

    public static class TreatmentInfoDTO {
        private String complications;
        private String treatmentMethod;

        public String getComplications() {
            return complications;
        }

        public void setComplications(String complications) {
            this.complications = complications;
        }

        public String getTreatmentMethod() {
            return treatmentMethod;
        }

        public void setTreatmentMethod(String treatmentMethod) {
            this.treatmentMethod = treatmentMethod;
        }
    }

    public static class WorkRestInfoDTO {
        private String sleepTime;
        private String wakeUpTime;

        public String getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(String sleepTime) {
            this.sleepTime = sleepTime;
        }

        public String getWakeUpTime() {
            return wakeUpTime;
        }

        public void setWakeUpTime(String wakeUpTime) {
            this.wakeUpTime = wakeUpTime;
        }
    }
}
