package com.sisensing.common.user;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.qiyukf.unicorn.api.Unicorn;
import com.sisensing.base.ViewModelFactory;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.LocalBleManager;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.entity.login.LoginBean;
import com.sisensing.common.entity.login.UserInfoBean;
import com.sisensing.common.entity.personalcenter.PersonalInfoEntity;
import com.sisensing.common.router.RouterActivityPath;
import com.sisensing.common.share.SharedViewModel;
import com.sisensing.common.utils.ConfigUtils;
import com.sisensing.common.utils.LanguageNumberUtils;
import com.tencent.mmkv.MMKV;

import java.util.Locale;


/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.user
 * @Author: f.deng
 * @CreateDate: 2021/2/25 10:08
 * @Description:
 */
public class UserInfoUtils {

    private static final String SP_USER = "sp_user";
    //用户登录成功保存token相关信息的key
    private static final String SP_USER_LOGIN = "user_login";
    //用户登录信息的key(用户的先关信息)
    private static final String SP_USER_ID = "user_id";
    private static final String SP_USER_INFO = "user_info";
    private static final String SP_USER_IS_LOGIN = "user_is_login";
    private static final String SP_USER_TOKEN = "user_token";
    private static final String SP_USER_ACCOUNT = "user_account";
    private static final String SP_USER_PASSWORD = "user_password";
    //用户血糖报警数量
    private static final String SP_USER_BS_WARNING_NUM = "sp_user_bs_warning_num";
    //用户打卡早餐时间key
    private static final String SP_USER_BREAKFAST = "sp_user_breakfast";

    //用户打卡晚餐时间key
    private static final String SP_USER_DINNER = "sp_user_dinner";
    //个人中心接口返回的个人信息
    private static final String SP_PERSONAL_INFO = "sp_personal_info";
    //个人中心早餐时间key
    private static final String SP_PERSONAL_CENTER_BREAKFAST = "sp_personal_center_breakfast";
    //个人中心午餐时间key
    private static final String SP_PERSONAL_CENTER_LUNCH = "sp_personal_center_lunch";
    //个人中心晚餐时间key
    private static final String SP_PERSONAL_CENTER_DINNER = "sp_personal_center_dinner";
    //个人中心入睡时间key
    private static final String SP_PERSONAL_CENTER_SLEEP = "sp_personal_center_sleep";
    //个人中心起床时间key
    private static final String SP_PERSONAL_CENTER_GET_UP = "sp_personal_center_get_up";
    //存储电流异常的index的key
    private static final String SP_ABNORMAL_CURRENT = "sp_abnormal_current";
    //电流异常次数
    private static final String SP_CURRENT_COUNT = "sp_current_count";
    //报警方式
    private static final String SP_ALARM_STYLE = "sp_alarm_style";
    //报警声音
    private static final String SP_ALARM_VOICE = "sp_alarm_voice";
    //是否正在上传血糖数据到服务器
    public static boolean isUploadBsDataToService = true;
    //血糖报警弹窗我知道了点击后保存的时间戳
    public static final String SP_ALARM_POP_I_KNOW_TIME = "sp_alarm_pop_i_know_time";
    //是否上传终端信息
    private static final String SP_IS_UPLOAD_TERMINAL_INFO = "sp_is_upload_terminal_info";
    //用户位置信息
    private static final String SP_USER_LOCATION_INFO = "sp_user_location_info";

    private static final String SP_LAST_VERSION_CODE = "last_version_code";
    //是否报警
    public static final String SP_IS_ALARM = "sp_is_alarm";
    //强制提醒
    public static final String SP_ALARM_FORCE = "sp_alarm_force";

    //是否设置主页面为血糖分享页面
    public static final String SP_SET_HOMEPAGE_REMOTEMONITOR = "sp_set_homepage_remoteMonitor";

    public static final String SP_IS_OPEN_GUIDE_PAGE = "sp_is_open_guide_page";


    /**
     * 存储电流异常index
     *
     * @param index
     */
    public static void putAcIndex(int index) {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;
        SPUtils.getInstance(deviceEntity.getDeviceName()).put(SP_ABNORMAL_CURRENT, index);

    }

    /**
     * 获取电流异常Index
     *
     * @return
     */
    public static int getAcIndex() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) {
            return 0;
        } else {
            return SPUtils.getInstance(deviceEntity.getDeviceName()).getInt(SP_ABNORMAL_CURRENT, 0);
        }
    }

    /**
     * 存储电流异常次数
     *
     * @param count
     */
    public static void putAcCount(int count) {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;
        SPUtils.getInstance(deviceEntity.getDeviceName()).put(SP_CURRENT_COUNT, count);
    }

    /**
     * 获取电流异常次数
     *
     * @return
     */
    public static int getAcCount() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) {
            return 0;
        } else {
            return SPUtils.getInstance(deviceEntity.getDeviceName()).getInt(SP_CURRENT_COUNT, 0);
        }
    }

    /**
     * 设备连接标识
     */
    public static boolean isDeviceConnected = false;
    /**
     * 设备是否在同步
     */
    public static boolean isSync = true;

    /**
     * 保存当前登录信息(token)
     */
    public static void saveLogin(LoginBean loginBean) {
        SPUtils.getInstance(SP_USER).put(SP_USER_TOKEN, loginBean.getAccess_token());
    }

    public static void putLogin() {
        SPUtils.getInstance(SP_USER).put(SP_USER_IS_LOGIN, true);
    }

    /**
     * 保存当前登录者的信息（用户相关信息）
     *
     * @param userInfoBean
     */
    public static void saveUserInfo(UserInfoBean userInfoBean) {

        SPUtils.getInstance(SP_USER).put(SP_USER_INFO, GsonUtils.toJson(userInfoBean));
        SPUtils.getInstance(SP_USER).put(SP_USER_ID, userInfoBean.getUserId());
    }


    /**
     * 获取当前已登录的用户信息
     *
     * @return
     */
    public static UserInfoBean getUserInfo() {
        String str = SPUtils.getInstance(SP_USER).getString(SP_USER_INFO);
        UserInfoBean userInfoBean = null;
        if (!TextUtils.isEmpty(str)) {
            Gson gson = new Gson();
            userInfoBean = gson.fromJson(str, new TypeToken<UserInfoBean>() {
            }.getType());
        }
        return userInfoBean;
    }


    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getUserId() {

        String userId = SPUtils.getInstance(SP_USER).getString(SP_USER_ID);

        if (ObjectUtils.isEmpty(userId) && getUserInfo() != null) {
            userId = getUserInfo().getUserId();
        }

        return userId;
    }


    /**
     * 判断登录状态
     *
     * @return
     */
    public static boolean isLogin() {
        return SPUtils.getInstance(SP_USER).getBoolean(SP_USER_IS_LOGIN, false);
    }


    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        String token = SPUtils.getInstance(SP_USER).getString(SP_USER_TOKEN);
        if (ObjectUtils.isEmpty(token)) {
            return "";
        } else {
            return token;
        }
    }


    public static void setAccountInfo(String account, String passWord) {
        SPUtils.getInstance(SP_USER).put(SP_USER_ACCOUNT, EncodeUtils.base64Encode2String(account.getBytes()));
        SPUtils.getInstance(SP_USER).put(SP_USER_PASSWORD, EncodeUtils.base64Encode2String(passWord.getBytes()));
    }

    public static String getAccount() {
        return new String(EncodeUtils.base64Decode(SPUtils.getInstance(SP_USER).getString(SP_USER_ACCOUNT)));
    }

    public static String getPassword() {
        return new String(EncodeUtils.base64Decode(SPUtils.getInstance(SP_USER).getString(SP_USER_PASSWORD)));
    }

    /**
     * 登出重新登录操作
     */
    public static void loginOut() {
        BleLog.dApp("退出登录,isDeviceConnected判定为false");
        isDeviceConnected = false;
        isUploadBsDataToService = true;
        isSync = false;
//        Unicorn.logout();
        //DeviceManager.getInstance().setDeviceEntity(null);
        LocalBleManager.getInstance().stopConnect();
        putWarningNum(0);
        ActivityUtils.finishAllActivities();
        SPUtils.getInstance(SP_USER).clear();
        setHomePageRemoteMonitor(false);
        putUploadTerminalInfo(true);
        ViewModelFactory.getApplicationScopeViewModel(SharedViewModel.class).resetDeviceLiveData();
        ARouter.getInstance().build(RouterActivityPath.Login.PAGE_LOGIN).navigation();
    }


    /**
     * 用户是否绑定过设备
     *
     * @return
     */
    public static boolean isBindDevice() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) {
            return false;
        } else {
            return !TextUtils.isEmpty(deviceEntity.getBlueToothNum());
        }
    }

    /**
     * 保存当前的血糖数量
     *
     * @param num 报警数量
     */
    public static void putWarningNum(int num) {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return;
        String deviceName = deviceEntity.getDeviceName();
        if (ObjectUtils.isEmpty(deviceName)) return;
        SPUtils.getInstance(deviceName).put(SP_USER_BS_WARNING_NUM, num);
    }

    /**
     * 获取报警数量
     *
     * @return
     */
    public static int getWarningNum() {
        DeviceEntity deviceEntity = DeviceManager.getInstance().getDeviceEntity();
        if (deviceEntity == null) return 0;
        String deviceName = deviceEntity.getDeviceName();
        if (ObjectUtils.isEmpty(deviceName)) {
            return 0;
        } else {
            return SPUtils.getInstance(deviceName).getInt(SP_USER_BS_WARNING_NUM, 0);
        }
    }

    /**
     * 保存用户早餐时间
     *
     * @param bkTime
     */
    public static void putUserBreakfastTime(String bkTime) {
        SPUtils.getInstance(SP_USER).put(SP_USER_BREAKFAST, bkTime);
    }

    /**
     * 获取用户早餐时间
     *
     * @return
     */
    public static String getUserBreakfastTime() {
        return SPUtils.getInstance(SP_USER).getString(SP_USER_BREAKFAST, "11:00");
    }

    /**
     * 保存用户晚餐时间
     *
     * @param bkTime
     */
    public static void putUserDinnerTime(String bkTime) {
        SPUtils.getInstance(SP_USER).put(SP_USER_DINNER, bkTime);
    }

    /**
     * 获取用户晚餐时间
     *
     * @return
     */
    public static String getUserDinnerTime() {
        return SPUtils.getInstance(SP_USER).getString(SP_USER_DINNER, "17:00");
    }

    /**
     * 保存个人信息
     *
     * @param entity
     */
    public static void putPersonalInfo(PersonalInfoEntity entity) {
        SPUtils.getInstance(SP_USER).put(SP_PERSONAL_INFO, GsonUtils.toJson(entity));
        PersonalInfoEntity.TargetDTO target = entity.getTarget();
        if (target != null) {
            float upper_1 = target.getUpper_1();
            float lower_1 = target.getLower_1();
            ConfigUtils.getInstance().setDefaultHigh(entity.getId(), upper_1);
            ConfigUtils.getInstance().setDefaultLow(entity.getId(), lower_1);
        }
    }

    /**
     * 获取个人信息
     *
     * @return
     */
    public static PersonalInfoEntity getPersonalInfo() {
        PersonalInfoEntity entity = null;

        String personalInfoStr = SPUtils.getInstance(SP_USER).getString(SP_PERSONAL_INFO);
        if (ObjectUtils.isNotEmpty(personalInfoStr)) {
            entity = GsonUtils.fromJson(personalInfoStr, PersonalInfoEntity.class);
        }

        return entity;
    }

    /**
     * 获取当前病人类型  ///糖尿病类型( 0: 未诊断/无糖尿病史   1: I型糖尿病   2: II型糖尿病   3: 特殊类型糖尿病   4: 妊娠期糖尿病   5:  老年或高风险I型糖尿病（属于1的子类）  6: 老年或高风险II型糖尿病（属于2的子类）7.其他
     *
     * @return
     */
    public static int getDrType() {
        int diabetesType = 0;
        PersonalInfoEntity entity = getPersonalInfo();
        if (entity != null) {
            diabetesType = entity.getDrType();
        }

        return diabetesType;
    }

    /**
     * 获取血糖报警上限
     *
     * @return
     */
    public static float getBsAlarmUpper() {
        PersonalInfoEntity entity = getPersonalInfo();
        float alarmUpper = Constant.BS_UPPER_DEFAULT;
        if (entity != null) {
            alarmUpper = getBsRangeValue(true, entity.getDrType());
            PersonalInfoEntity.AlarmDTO alarmDTO = entity.getAlarm();
            if (alarmDTO != null) {
                String upper = String.valueOf(alarmDTO.getUpper());
                if (ObjectUtils.isNotEmpty(upper)) {
                    alarmUpper = LanguageNumberUtils.string2Float(upper);
                }
            }
        }

        return alarmUpper;
    }

    /**
     * 获取血糖上限
     *
     * @return
     */
    public static float getBsRangeUpper() {
        PersonalInfoEntity entity = getPersonalInfo();
        float upper = Constant.BS_UPPER_DEFAULT;
        if (entity != null) {
            upper = getBsRangeValue(true, entity.getDrType());
            PersonalInfoEntity.TargetDTO targetDTO = entity.getTarget();
            if (targetDTO != null) {
                float upper1 = targetDTO.getUpper_1();
                if (upper1 != 0) {
                    upper = upper1;
                }
            }
        }
        return upper;
    }

    /**
     * 获取血糖下限
     *
     * @return
     */
    public static float getBsRangeLower() {
        float lower = Constant.BS_LOWER_DEFAULT;
        PersonalInfoEntity entity = getPersonalInfo();
        if (entity != null) {
            lower = getBsRangeValue(false, entity.getDrType());
            PersonalInfoEntity.TargetDTO targetDTO = entity.getTarget();
            if (targetDTO != null) {
                float lower1 = targetDTO.getLower_1();
                if (lower1 != 0) {
                    lower = lower1;
                }
            }
        }
        return lower;
    }

    public static float getBsRangeValue(boolean upper, int drType) {
        //drType 0: 未诊断/无糖尿病史   1: I型糖尿病   2: II型糖尿病 3: 特殊类型糖尿病  4: 妊娠期糖尿病 5:Elderly diabetes 6 High-risk diabetes 7:其他
        if (upper) {
            switch (drType) {
                case 1:
                case 2:
                    return Constant.BS_UPPER_TYPE_1_2;
                case 4:
                    return Constant.BS_UPPER_TYPE_GESTATION;
                case 5:
                case 6:
                    return Constant.BS_UPPER_TYPE_ELDER_HIGH;
                default:
                    return Constant.BS_UPPER_DEFAULT;
            }
        } else {
            switch (drType) {
                case 4:
                    return Constant.BS_LOWER_GESTATION;
                case 5:
                case 6:
                    return Constant.BS_LOWER_ELDER_HIGH;
                default:
                    return Constant.BS_LOWER_DEFAULT;
            }
        }
    }

    /**
     * 获取血糖报警下限
     *
     * @return
     */
    public static float getBsAlarmLower() {
        float alarmLower = Constant.BS_LOWER_DEFAULT;
        PersonalInfoEntity entity = getPersonalInfo();
        if (entity != null) {
            alarmLower = getBsRangeValue(false, entity.getDrType());
            PersonalInfoEntity.AlarmDTO alarmDTO = entity.getAlarm();
            if (alarmDTO != null) {
                String lower = String.valueOf(alarmDTO.getLower());
                if (ObjectUtils.isNotEmpty(lower)) {
                    alarmLower = LanguageNumberUtils.string2Float(lower);
                }
            }
        }

        return alarmLower;
    }

    /**
     * 获取血糖上限
     *
     * @return
     */
    public static float getBsRangeUpperFromDrType() {
        float upper;
        int drType = getDrType();
        if (drType == 1 || drType == 2 || drType == 3 || drType == 5 || drType == 6) {
            //I型、II型糖尿病
            upper = 10.0f;
        } else if (drType == 4) {
            //妊娠型糖尿病
            upper = 7.8f;
        } else {
            //正常人或其他
            upper = 7.8f;
        }
        return upper;
    }

    /**
     * 获取血糖下限
     *
     * @return
     */
    public static float getBsRangeLowerFromDrType() {
        float lower;
        int drType = getDrType();
        if (drType == 1 || drType == 2 || drType == 3 || drType == 5 || drType == 6) {
            //I型、II型糖尿病
            lower = 3.9f;
        } else if (drType == 4) {
            //妊娠型糖尿病
            lower = 3.5f;
        } else {
            //正常人或其他
            lower = 3.9f;
        }
        return lower;
    }

    public static String[] getTirPercent() {
        //1表示1型糖尿病、2表示2型糖尿病、4表示妊娠糖尿病、3表示特殊糖尿病、0表示未确诊/无糖尿病史 7其他)
        String[] percent = new String[]{"0.7", "0.12", "0.17"};
        PersonalInfoEntity entity = getPersonalInfo();
        if (entity != null) {
            int drType = entity.getDrType();
            if (drType == 0 || drType == 7) {
                percent[0] = "0.7";
                percent[1] = "0.12";
                percent[2] = "0.17";
            } else if (drType == 1 || drType == 2 || drType == 3 || drType == 4) {
                if (drType == 4) {
                    percent[0] = "0.9";
                } else {
                    percent[0] = "0.7";
                }
                percent[1] = "0.04";
                percent[2] = "0.25";
            } else {
                percent[0] = "0.5";
                percent[1] = "0.01";
                percent[2] = "0.5";
            }
        }


        return percent;
    }

    /**
     * 设置血糖报警开关
     *
     * @param isAlarm
     */
    public static void setBsAlarmSwitch(boolean isAlarm) {
        SPUtils.getInstance(getUserId()).put(SP_IS_ALARM, isAlarm);
    }

    public static void setBsAlarmForce(boolean isForce) {
        SPUtils.getInstance(getUserId()).put(SP_ALARM_FORCE, isForce);
    }

    /**
     * 获取血糖报警开关开启或关闭
     *
     * @return
     */
    public static boolean getBsAlarmSwitch() {
        return SPUtils.getInstance(getUserId()).getBoolean(SP_IS_ALARM, true);
    }

    public static boolean getBsForceAlarm() {
        return SPUtils.getInstance(getUserId()).getBoolean(SP_ALARM_FORCE, false);
    }

    /**
     * 保存报警方式
     *
     * @param position
     */
    public static void putAlarStyle(int position) {
        SPUtils.getInstance(SP_USER).put(SP_ALARM_STYLE, position - 1);
    }

    /**
     * 获取报警方式 默认震动
     *
     * @return
     */
    public static int getAlarmStyle() {
        return SPUtils.getInstance(SP_USER).getInt(SP_ALARM_STYLE, 1);
    }

    /**
     * 保存报警声音
     *
     * @param position
     */
    public static void putAlarVoice(int position) {
        SPUtils.getInstance(SP_USER).put(SP_ALARM_VOICE, position - 1);
    }

    /**
     * 获取报警方式
     *
     * @return
     */
    public static int getAlarmVoice() {
        return SPUtils.getInstance(SP_USER).getInt(SP_ALARM_VOICE, 0);
    }

    /**
     * 保存点击报警弹窗我知道了的时间戳
     *
     * @param mill
     */
    public static void putAlarmIKnowTime(long mill) {
        SPUtils.getInstance(SP_USER).put(SP_ALARM_POP_I_KNOW_TIME, mill);
    }

    /**
     * 获取点击报警弹窗我知道了的时间戳
     *
     * @return
     */
    public static long getAlarmIKnowTime() {
        return SPUtils.getInstance(SP_USER).getLong(SP_ALARM_POP_I_KNOW_TIME, 0);
    }

    /**
     * 保存是否上传终端信息
     *
     * @param isUpLoad
     */
    public static void putUploadTerminalInfo(boolean isUpLoad) {
        SPUtils.getInstance(SP_USER).put(SP_IS_UPLOAD_TERMINAL_INFO, isUpLoad);
    }

    /**
     * 获取是否
     *
     * @return
     */
    public static boolean getUploadTerminalInfo() {
        return SPUtils.getInstance(SP_USER).getBoolean(SP_IS_UPLOAD_TERMINAL_INFO, true);
    }

    /**
     * 保存是否第一次打开app
     *
     * @param isFirstOpenApp
     */
    public static void putFirstOpenApp(boolean isFirstOpenApp) {
        SPUtils.getInstance(SP_USER).put(Constant.IS_FIRST_OPEN_APP, isFirstOpenApp);
    }

    /**
     * 获取是否第一次打开app
     *
     * @return
     */
    public static boolean getIsFirstOpenApp() {
        return SPUtils.getInstance(SP_USER).getBoolean(Constant.IS_FIRST_OPEN_APP, true);
    }

    /**
     * 保存位置信息
     *
     * @param address
     */
    public static void putLocationInfo(String address) {
        SPUtils.getInstance(SP_USER).put(SP_USER_LOCATION_INFO, address);
    }

    /**
     * 获取位置信息
     */
    public static String getLocationInfo() {
        return SPUtils.getInstance(SP_USER).getString(SP_USER_LOCATION_INFO, "");
    }


    public static int getLastVersionCode() {
        return SPUtils.getInstance(SP_USER).getInt(SP_LAST_VERSION_CODE, 0);
    }

    public static void setLastVersionCode(int versionCode) {
        SPUtils.getInstance(SP_USER).put(SP_LAST_VERSION_CODE, versionCode);
    }


    /**
     * 保存是否第一次打开app
     *
     * @param isFirstOpenApp
     */
    public static void putFirstOpenPrivacyPop(boolean isFirstOpenApp) {
        SPUtils.getInstance(SP_USER).put(Constant.LAUNCHER_PAGE_FIRST_OPEN_PRIVACY_POP, isFirstOpenApp);
    }

    /**
     * 获取是否第一次打开app
     *
     * @return
     */
    public static boolean getFirstOpenPrivacyPop() {
        return SPUtils.getInstance(SP_USER).getBoolean(Constant.LAUNCHER_PAGE_FIRST_OPEN_PRIVACY_POP, true);
    }

    public static void saveLanguage(int select) {
        SPUtils.getInstance(SP_USER).put(Constant.TAG_LANGUAGE, select);
    }

    public static int getSelectLanguage() {
        return SPUtils.getInstance(SP_USER).getInt(Constant.TAG_LANGUAGE, 1);
    }


    public static Locale getSystemCurrentLocal() {
        return Constant.systemCurrentLocal;
    }

    public static void setSystemCurrentLocal(Locale local) {
        Constant.systemCurrentLocal = local;
    }

    /**
     * 设置主页面为血糖分享页面
     *
     * @param isHomePageRemoteMonitor
     */
    public static void setHomePageRemoteMonitor(boolean isHomePageRemoteMonitor) {
        SPUtils.getInstance(getUserId()).put(SP_SET_HOMEPAGE_REMOTEMONITOR, isHomePageRemoteMonitor);
    }

    /**
     * 获取主页面是否是血糖分享页面
     *
     * @return
     */
    public static boolean getHomePageRemoteMonitor() {
        return SPUtils.getInstance(getUserId()).getBoolean(SP_SET_HOMEPAGE_REMOTEMONITOR, false);
    }

    /**
     * 设置是否打开起始页
     * @param isOpen
     */
    public static void setOpenGuidePage(boolean isOpen) {
        SPUtils.getInstance(getUserId()).put(SP_IS_OPEN_GUIDE_PAGE, isOpen);
    }

    public static boolean getOpenGuidePage() {
        return SPUtils.getInstance(getUserId()).getBoolean(SP_IS_OPEN_GUIDE_PAGE, true);
    }
}
