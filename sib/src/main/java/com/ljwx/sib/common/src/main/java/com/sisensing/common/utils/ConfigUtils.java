package com.sisensing.common.utils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.user.UserInfoUtils;

/**
 * @ProjectName: CGM_hospital_1.0
 * @Package: com.sisensing.sijoy.utils
 * @Author: f.deng
 * @CreateDate: 2021/3/2 16:55
 * @Description:
 */
public class ConfigUtils {

    private static final String SENSITIVITY = "sensitivity";
    private static final String INTERCEPT = "intercept";
    private static final String DEFAULT_HIGH = "defaultHigh";
    private static final String DEFAULT_LOW = "defaultLow";
    private static final String ERRCURRENT_TIMEMILLS = "errCurrentTimeMills";
    private static final String FIRSTTIME = "firstTime";
    private static final String COUNTDOWN_TIMEMILLS = "countDownTimeMills";
    private static final String FIGURE = "figure";
    private static final String FIGURE_TIME = "figure_time";
    private static final String LAST_ALARMTIMEMILLS = "lastAlarmTimeMills";
    private static final String CGM_INVALID = "cgmInvalid";
    private static final String REMOVE = "remove";
    private static final String LAST_INDEX = "lastIndex";
    private static final String JNI_CONFIGS = "jniConfigs";

    public final static String UNIT = "unit";
    public final static String UNIT_MMOL = "mmol/L";
    public final static String UNIT_MG = "mg/dL";

    private Gson mGson;

    private ConfigUtils() {
        mGson = new Gson();
    }

    private static class Inner {
        private static ConfigUtils INSTANCE = new ConfigUtils();
    }

    public static ConfigUtils getInstance() {
        return Inner.INSTANCE;
    }


    public void setLastIndex(String macName, int index) {
        SPUtils.getInstance(macName).put(LAST_INDEX, index);
    }

    public int getLastIndex(String macName) {
        return SPUtils.getInstance(macName).getInt(LAST_INDEX, 0) + 1;
    }


    public void setDefaultHigh(String userId, float defaultHigh) {

        SPUtils.getInstance(userId).put(DEFAULT_HIGH, defaultHigh);

    }

    public float getDefaultHigh(String userId) {

        return SPUtils.getInstance(userId).getFloat(DEFAULT_HIGH, 11.1f);

    }

    public static void setDefaultLow(String userId, float defaultLow) {

        SPUtils.getInstance(userId).put(DEFAULT_LOW, defaultLow);

    }

    public float getDefaultLow(String userId) {

        return SPUtils.getInstance(userId).getFloat(DEFAULT_LOW, 4.4f);

    }

    public void setErrCurrentTimeMills(String userId, long errCurrentTimeMills) {

        SPUtils.getInstance(userId).put(ERRCURRENT_TIMEMILLS, errCurrentTimeMills);

    }

    public long getErrCurrentTimeMills(String userId) {

        return SPUtils.getInstance(userId).getLong(ERRCURRENT_TIMEMILLS);

    }


    public void setFirstTime(long firstTime) {

        SPUtils.getInstance(DeviceManager.getInstance().getDeviceEntity().getDeviceName()).put(FIRSTTIME, firstTime);

    }

    public long getFirstTime() {

        return SPUtils.getInstance(DeviceManager.getInstance().getDeviceEntity().getDeviceName()).getLong(FIRSTTIME);

    }


    public void setFigure(String userId, float figure, long timeMill) {

        SPUtils.getInstance(userId).put(FIGURE, figure);
        SPUtils.getInstance(userId).put(FIGURE_TIME, timeMill);

    }

    public float getFigure(String userId) {

        return SPUtils.getInstance(userId).getFloat(FIGURE);

    }

    public float getFigureTime(String userId) {

        return SPUtils.getInstance(userId).getFloat(FIGURE_TIME);

    }


    public void setLastAlarmTimeMills(String userId, long lastAlarmTimeMills) {

        SPUtils.getInstance(userId).put(LAST_ALARMTIMEMILLS, lastAlarmTimeMills);

    }

    public long getLastAlarmTimeMills(String userId) {

        return SPUtils.getInstance(userId).getLong(LAST_ALARMTIMEMILLS);

    }

    public void setCgmInvalid(String userId, boolean cgmInvalid) {

        SPUtils.getInstance(userId).put(CGM_INVALID, cgmInvalid);

    }

    public boolean getCgmInvalid(String userId) {

        return SPUtils.getInstance(userId).getBoolean(CGM_INVALID);

    }

    public void setCgmRemove(String userId, boolean remove) {

        SPUtils.getInstance(userId).put(REMOVE, remove);

    }

    public boolean getCgmRemove(String userId) {

        return SPUtils.getInstance(userId).getBoolean(REMOVE);

    }

    public boolean unitIsMmol(String userId) {
        return BgUnitUtils.isUnitMol(getUnit(userId));
    }

    public boolean unitIsMmol() {
        String unit = getUnit(UserInfoUtils.getUserId());
        return BgUnitUtils.isUnitMol(unit);
    }

    public String getUnit(String userId) {
//        String spUnit = SPUtils.getInstance(userId).getString(UNIT, BgUnitUtils.getMolUnit());
//        return BgUnitUtils.isMol(spUnit) ? BgUnitUtils.getMolUnit() : BgUnitUtils.getMgUnit();
        return BgUnitUtils.getUserUnit(userId);
    }

    public void setUnit(String userId, boolean isMmol) {
        BgUnitUtils.setUserUnit(userId, isMmol);
//        SPUtils.getInstance(userId).put(UNIT, isMmol ? BgUnitUtils.getMolUnit() : BgUnitUtils.getMgUnit());
        GlucoseUtils.initUnitFromUser();
    }

}
