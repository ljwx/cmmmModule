package com.sisensing.common.utils;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.user.UserInfoUtils;

import java.util.List;
import java.util.Locale;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.utils
 * @Author: f.deng
 * @CreateDate: 2021/3/15 15:51
 * @Description:
 */
public class GlucoseUtil {

    /**
     * mmol/L、mg/dL单位转换
     *
     * @param mmol
     * @param
     * @return
     */
    public static float getTransferValue(float mmol) {
        if (ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mmol));
        } else {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mmol * Constant.BS_UNIT_CHANGE_NUMBER));
        }
    }

    public static String getTransferValueString(float mmol) {
        if (ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mmol)) +"";
        } else {
            return ((int)(mmol * Constant.BS_UNIT_CHANGE_NUMBER)) + "";
        }
    }

    public static float getTransferMgValue(float mg ) {
        if (ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg / Constant.BS_UNIT_CHANGE_NUMBER));
        } else {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg ));
        }
    }

    public static String getTransferMgValueString(float mg ) {
        if (ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg / Constant.BS_UNIT_CHANGE_NUMBER)) + "";
        } else {
            return ((int) mg) + "";
        }
    }

    public static float getTransferValue(float mmol,boolean isMmol) {
        if (isMmol) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mmol));
        } else {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mmol * Constant.BS_UNIT_CHANGE_NUMBER));
        }
    }

    public static float getTransferMgValue(float mg,boolean isMmol) {
        if (isMmol) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg / Constant.BS_UNIT_CHANGE_NUMBER));
        } else {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg ));
        }
    }

    public static String getTransferMgValueString(float mg,boolean isMmol) {
        if (isMmol) {
            return Float.parseFloat(String.format(Locale.US, "%.1f", mg / Constant.BS_UNIT_CHANGE_NUMBER)) + "";
        } else {
            return ((int) mg) + "";
        }
    }

    /**
     * mg转mmol
     * @param mg
     * @return
     */
    public static float mgToMmol(float mg){
        return Float.parseFloat(String.format(Locale.US, "%.1f", mg/Constant.BS_UNIT_CHANGE_NUMBER));
    }

    /**
     * mmol转mg
     * @param mmol
     * @return
     */
    public static float mmolToMg(float mmol){
        return Float.parseFloat(String.format(Locale.US, "%.1f", mmol * Constant.BS_UNIT_CHANGE_NUMBER));
    }

    public static int mmolToMgV2(float mmol){
        return ((int) (mmol * Constant.BS_UNIT_CHANGE_NUMBER));
    }

    /**
     * 血糖指标达标率 正常（TIR）
     * 选中时间范围内，血糖在目标范围内的数量占比
     *
     * @return
     */
    public static String getTIR(List<BloodGlucoseEntity> entities, String userId) {
        //达标数量
        float tirCount = 0;
        //总数量
        float amount = entities.size();

        float min = getTransferValue(ConfigUtils.getInstance().getDefaultLow(userId));

        float max = getTransferValue(ConfigUtils.getInstance().getDefaultHigh(userId));

        for (int i = 0; i < amount; i++) {
            float glucoseValue = getTransferValue(entities.get(i).getGlucoseValue());
            if (glucoseValue >= min && glucoseValue <= max) {
                tirCount++;
            }
        }

        return MathUtil.getTransferPointData((tirCount / amount) * 100, 2) + "";
    }

    public static void transferDatabaseFingerBlood(ActionRecordEntity entity) {
        String compatUnit = entity.getSgUnit();
        String detail = entity.getEventDetail();
        Log.d("从数据库获取血糖事件", "单位:"+compatUnit+"-值:"+detail);
        if (!StringUtils.isEmpty(compatUnit) && !StringUtils.isEmpty(detail)) {
            float value = LanguageNumberUtils.string2Float(detail);
            boolean isMmol = BgUnitUtils.isMolCompat(compatUnit);
            if (isMmol) {
                if (!ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
                    entity.setEventDetail(GlucoseUtil.getTransferValue(value, false) + "");
                }
            } else {
                if (ConfigUtils.getInstance().unitIsMmol(UserInfoUtils.getUserId())) {
                    entity.setEventDetail(GlucoseUtil.getTransferMgValue(value, true) + "");
//                    entity.setEventDetail(GlucoseUtils.getConvertValue(value,false, true));
                }
            }
//            entity.setEventDetail(GlucoseUtils.getConvertValue(value, isMmol, BgUnitUtils.isUserMol()));
        }
    }

}
