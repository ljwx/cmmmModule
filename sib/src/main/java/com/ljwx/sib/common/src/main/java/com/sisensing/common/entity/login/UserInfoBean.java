package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/3/5 9:09
 * @desc 用户信息实体类
 */
public class UserInfoBean {

    /**
     * token : a03647bf-83d1-4854-8bc5-c62f89481b0c
     * tokenUserType : USER
     * userId : 1367708983770615808
     * userName : 13058016061
     * loginTime : 1614922207462
     * expireTime : 1614965407462
     * ipaddr : 192.168.1.92
     * permissions : null
     * roles : null
     * userInfo : {"id":1367708983770615808,"userAccount":"13058016061","phone":"13058016061","loginIp":null,"loginTime":null,"userName":"13058016061","nickName":"13058016061","avatar":null,"sex":null,"height":null,"weight":null,"birthday":null,"drType":-1,"courseOfDisease":null,"targetUpper":null,"targetLower":null,"crisisUpper":null,"crisisLower":null}
     */

    private String token;
    private String tokenUserType;
    private String userId;
    private String userName;
    private long loginTime;
    private long expireTime;
    private String ipaddr;
    private String permissions;
    private String roles;
    private String email;
    private UserInfoDTO userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenUserType() {
        return tokenUserType;
    }

    public void setTokenUserType(String tokenUserType) {
        this.tokenUserType = tokenUserType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoDTO {
        /**
         * id : 1367708983770615808
         * userAccount : 13058016061
         * phone : 13058016061
         * loginIp : null
         * loginTime : null
         * userName : 13058016061
         * nickName : 13058016061
         * avatar : null
         * sex : null
         * height : null
         * weight : null
         * birthday : null
         * drType : -1
         * courseOfDisease : null
         * targetUpper : null
         * targetLower : null
         * crisisUpper : null
         * crisisLower : null
         */

        private long id;
        private String userAccount;
        private String phone;
        private String loginIp;
        private String loginTime;
        private String userName;
        private String nickName;
        private String avatar;
        private String sex;
        private String height;
        private String weight;
        private String birthday;
        private int drType;
        private String courseOfDisease;
        private String targetUpper;
        private String targetLower;
        private String crisisUpper;
        private String crisisLower;
        private boolean emptyPassword;//是否需要设置密码(true需要 false不需要)

        private boolean boundFlag; //用户是否绑定过设备

        private int latestDeviceStatus; //最新设备状态

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(String loginIp) {
            this.loginIp = loginIp;
        }

        public Object getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getDrType() {
            return drType;
        }

        public void setDrType(int drType) {
            this.drType = drType;
        }

        public String getCourseOfDisease() {
            return courseOfDisease;
        }

        public void setCourseOfDisease(String courseOfDisease) {
            this.courseOfDisease = courseOfDisease;
        }

        public String getTargetUpper() {
            return targetUpper;
        }

        public void setTargetUpper(String targetUpper) {
            this.targetUpper = targetUpper;
        }

        public String getTargetLower() {
            return targetLower;
        }

        public void setTargetLower(String targetLower) {
            this.targetLower = targetLower;
        }

        public String getCrisisUpper() {
            return crisisUpper;
        }

        public void setCrisisUpper(String crisisUpper) {
            this.crisisUpper = crisisUpper;
        }

        public String getCrisisLower() {
            return crisisLower;
        }

        public void setCrisisLower(String crisisLower) {
            this.crisisLower = crisisLower;
        }

        public boolean isEmptyPassword() {
            return emptyPassword;
        }

        public void setEmptyPassword(boolean emptyPassword) {
            this.emptyPassword = emptyPassword;
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
    }
}
